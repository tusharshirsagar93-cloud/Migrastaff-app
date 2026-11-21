const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_worker_01";
const BOB_UID = "bob_worker_02";
const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

const tsNow = () => new Date(Date.now() - 1000);

function validProfile(uid) {
  return {
    userId: uid,
    username: "jasur_welder",
    surname: "Karimov",
    firstName: "Jasur",
    countryCode: "UZ",
    profession: "Welder",
    experienceYears: 7,
    expectedSalary: 95000,
    languages: "Russian, Uzbek",
    skills: "MIG welding, Pipe fitting",
    about: "7 years pipeline welding experience.",
    phone: "+79120000001",
    dateOfBirth: "1992-04-12",
    verStatus: "pending",
    photoCount: 2,
    createdAt: tsNow(),
    updatedAt: tsNow(),
  };
}

function validFavorite(uid, workerId = "1") {
  return {
    userId: uid,
    workerId,
    workerName: "Jasur Karimov",
    profession: "Welder",
    countryCode: "UZ",
    salaryRub: 95000,
    createdAt: tsNow(),
  };
}

function validMessage(uid) {
  return {
    userId: uid,
    senderRole: "candidate",
    textOriginal: "Hello Director, my documents are ready.",
    textTranslated: "Здравствуйте, директор, мои документы готовы.",
    lang: "en",
    createdAt: tsNow(),
  };
}

function validCryptoReceipt(uid) {
  return {
    userId: uid,
    refId: "MS-CR-102938",
    planKey: "pro",
    coinKey: "USDT_TRC20",
    amountCrypto: "5.39 USDT",
    amountRub: 499,
    txHash: "abcdef1234567890abcdef",
    payerContact: "jasur@example.com",
    status: "submitted",
    createdAt: tsNow(),
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated caller is denied all reads and writes", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("profiles").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("favorites").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("messages").get()
  );
  await assertFails(
    unauthDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("crypto_payments")
      .get()
  );
  await assertFails(
    unauthDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("profiles")
      .doc("candidate")
      .set(validProfile(ALICE_UID))
  );
});

test("Cross-user reads and writes are denied", async () => {
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    const db = ctx.firestore();
    await db
      .collection("users")
      .doc(ALICE_UID)
      .collection("profiles")
      .doc("candidate")
      .set(validProfile(ALICE_UID));
    await db
      .collection("users")
      .doc(ALICE_UID)
      .collection("messages")
      .doc("m1")
      .set(validMessage(ALICE_UID));
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("profiles")
      .doc("candidate")
      .get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("messages").get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("favorites")
      .doc("1")
      .set(validFavorite(ALICE_UID, "1"))
  );
});

test("Candidate profile CRUD enforces schema, immutability, and verStatus", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const ref = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("profiles")
    .doc("candidate");

  // Reject verified spoof on create
  await assertFails(
    ref.set({ ...validProfile(ALICE_UID), verStatus: "verified" })
  );

  // Reject ghost field
  await assertFails(
    ref.set({ ...validProfile(ALICE_UID), adminOverride: true })
  );

  // Allow valid create
  await assertSucceeds(ref.set(validProfile(ALICE_UID)));

  // Reject escalating verStatus on update
  await assertFails(
    ref.update({ verStatus: "verified", updatedAt: tsNow() })
  );

  // Allow valid field update
  await assertSucceeds(
    ref.update({ expectedSalary: 105000, updatedAt: tsNow() })
  );

  await assertSucceeds(ref.get());
});

test("Favorites, messages, and crypto_payments enforce rules", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();

  const favRef = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("favorites")
    .doc("1");
  await assertSucceeds(favRef.set(validFavorite(ALICE_UID, "1")));
  // Mismatched workerId vs doc id fails
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("favorites")
      .doc("2")
      .set(validFavorite(ALICE_UID, "1"))
  );
  await assertSucceeds(favRef.delete());

  // Messages: candidate role allowed, director spoof denied
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("messages").add({
      ...validMessage(ALICE_UID),
      senderRole: "director",
    })
  );
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("messages")
      .add(validMessage(ALICE_UID))
  );

  // Crypto payments: submitted status allowed, confirmed spoof denied, delete denied
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("crypto_payments")
      .add({ ...validCryptoReceipt(ALICE_UID), status: "confirmed" })
  );
  const payRef = await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("crypto_payments")
      .add(validCryptoReceipt(ALICE_UID))
  );
  await assertFails(payRef.delete());
});
