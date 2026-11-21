import pathlib, re

# 1. Fix adaptive icon safe-zone with inset (22%)
fg = pathlib.Path("app/src/main/res/drawable/ic_launcher_foreground.xml")
fg.write_text('<?xml version="1.0" encoding="utf-8"?>\n<inset xmlns:android="http://schemas.android.com/apk/res/android"\n    android:drawable="@drawable/ic_migrastaff_symbol"\n    android:inset="22%" />\n')

# 2. Fix sendMessage translation (detect Cyrillic, HttpURLConnection + MyMemory fallback)
rp = pathlib.Path("app/src/main/java/com/example/data/firebase/MigraFirestoreRepository.kt")
s = rp.read_text()

new_fn = '''    private fun translateTextSync(text: String, targetLang: String): String {
        val clean = text.trim()
        if (clean.isEmpty()) return ""
        val q = java.net.URLEncoder.encode(clean, "UTF-8")
        // 1. Google Translate with User-Agent
        runCatching {
            val conn = (java.net.URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=$targetLang&dt=t&q=$q").openConnection() as java.net.HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val arr = org.json.JSONArray(body).getJSONArray(0)
            val res = buildString { for (i in 0 until arr.length()) append(arr.getJSONArray(i).getString(0)) }.trim()
            if (res.isNotEmpty() && !res.equals(clean, ignoreCase = true)) return res
        }
        // 2. Fallback: MyMemory API
        val pair = if (targetLang == "ru") "en|ru" else "ru|en"
        runCatching {
            val conn = (java.net.URL("https://api.mymemory.translated.net/get?q=$q&langpair=$pair").openConnection() as java.net.HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val res = org.json.JSONObject(body).optJSONObject("responseData")?.optString("translatedText", "")?.trim().orEmpty()
            if (res.isNotEmpty() && !res.equals(clean, ignoreCase = true)) return res
        }
        return clean
    }

    suspend fun sendMessage(text: String, translated: String, langCode: String): Result<Unit> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val path = "candidates/$uid/messages"
        return try {
            val clean = text.trim()
            val isRuText = clean.any { it in '\\u0400'..'\\u04FF' }
            val target = if (isRuText) "en" else "ru"
            val trans = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                translateTextSync(clean, target)
            }
            val textRu = if (isRuText) clean else trans
            val textEn = if (isRuText) trans else clean
            val msgMap = hashMapOf<String, Any>(
                "sender" to "candidate",
                "senderRole" to "candidate",
                "text" to clean,
                "textOrig" to clean,
                "origText" to clean,
                "textOriginal" to clean,
                "textTranslated" to trans,
                "textEn" to textEn,
                "textRu" to textRu,
                "lang" to if (isRuText) "ru" else "en",
                "createdAt" to FieldValue.serverTimestamp()
            )
            messagesColRef(uid).add(msgMap).await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(FirestoreErrorHandler.handleFirestoreError(error, OperationType.CREATE, path))
        }
    }

'''

pattern = r'(?:    private fun translateTextSync[\s\S]*?)?    suspend fun sendMessage\([\s\S]*?(?=    fun observeCryptoReceipts\()'
assert re.search(pattern, s), "sendMessage not found!"
s = re.sub(pattern, new_fn, s, count=1)
rp.write_text(s)
print("CHAT + LOGO FIXED OK!")
