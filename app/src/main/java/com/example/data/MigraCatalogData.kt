package com.example.data

import com.example.R
import com.example.model.AppLang
import com.example.model.CountryInfo
import com.example.model.CryptoCoin
import com.example.model.CryptoPlan
import com.example.model.FxCurrency
import com.example.model.FxPreset
import com.example.model.InfoTopic
import com.example.model.JobVacancy
import com.example.model.ProfessionCategory
import com.example.model.TeamMember
import com.example.model.WorkerProfile

object MigraCatalogData {

    val countries = listOf(
        CountryInfo("IN", "India", "Индия", "🇮🇳"),
        CountryInfo("UZ", "Uzbekistan", "Узбекистан", "🇺🇿"),
        CountryInfo("TJ", "Tajikistan", "Таджикистан", "🇹🇯"),
        CountryInfo("KG", "Kyrgyzstan", "Кыргызстан", "🇰🇬"),
        CountryInfo("VN", "Vietnam", "Вьетнам", "🇻🇳"),
        CountryInfo("PH", "Philippines", "Филиппины", "🇵🇭"),
        CountryInfo("KZ", "Kazakhstan", "Казахстан", "🇰🇿"),
        CountryInfo("AM", "Armenia", "Армения", "🇦🇲"),
        CountryInfo("RS", "Serbia", "Сербия", "🇷🇸"),
        CountryInfo("NP", "Nepal", "Непал", "🇳🇵"),
        CountryInfo("TR", "Turkey", "Турция", "🇹🇷"),
        CountryInfo("CN", "China", "Китай", "🇨🇳")
    )

    val categories = listOf(
        ProfessionCategory("welder", "Welder", 124),
        ProfessionCategory("cook", "Cook", 210),
        ProfessionCategory("driver", "Driver", 186),
        ProfessionCategory("nurse", "Nurse", 95),
        ProfessionCategory("const", "Construction Worker", 540),
        ProfessionCategory("elec", "Electrician", 88),
        ProfessionCategory("ware", "Warehouse Worker", 160),
        ProfessionCategory("mason", "Mason", 142),
        ProfessionCategory("house", "Housekeeper", 175),
        ProfessionCategory("mech", "Mechanic", 98),
        ProfessionCategory("it", "IT Support", 64),
        ProfessionCategory("farm", "Farmer", 76)
    )

    private val profRu = mapOf(
        "Welder" to "Сварщик",
        "Cook" to "Повар",
        "Driver" to "Водитель",
        "Nurse" to "Медсестра / Медбрат",
        "Construction Worker" to "Строитель",
        "Electrician" to "Электрик",
        "Warehouse Worker" to "Работник склада",
        "Mason" to "Каменщик",
        "Housekeeper" to "Горничная",
        "Mechanic" to "Механик",
        "IT Support" to "IT-поддержка",
        "Farmer" to "Фермер"
    )

    private val skillRu = mapOf(
        "MIG welding" to "MIG-сварка",
        "Pipe fitting" to "Монтаж труб",
        "Blueprint reading" to "Чтение чертежей",
        "Indian cuisine" to "Индийская кухня",
        "Bakery" to "Выпечка",
        "HACCP" to "ХАССП",
        "Category C+E" to "Категория C+E",
        "Long-haul" to "Дальние рейсы",
        "Mechanics" to "Механика",
        "Industrial wiring" to "Пром. электромонтаж",
        "PLC" to "ПЛК",
        "Safety codes" to "Техника безопасности",
        "ICU care" to "Реанимация (ОРИТ)",
        "First aid" to "Первая помощь",
        "Phlebotomy" to "Забор крови",
        "Masonry" to "Кладка",
        "Formwork" to "Опалубка",
        "Scaffolding" to "Строительные леса",
        "Hotel housekeeping" to "Уборка в отелях",
        "Laundry" to "Прачечная",
        "Management" to "Управление",
        "Diesel engines" to "Дизельные двигатели",
        "Hydraulics" to "Гидравлика",
        "Welding" to "Сварка",
        "Forklift" to "Погрузчик",
        "Inventory" to "Инвентаризация",
        "Packing" to "Упаковка",
        "Bricklaying" to "Кирпичная кладка",
        "Tiling" to "Укладка плитки",
        "Plastering" to "Штукатурка",
        "Greenhouse" to "Теплицы",
        "Livestock" to "Животноводство",
        "Tractor" to "Трактор",
        "Networking" to "Сети",
        "Helpdesk" to "Техподдержка",
        "Linux" to "Linux"
    )

    private val langRu = mapOf(
        "Russian" to "Русский",
        "Uzbek" to "Узбекский",
        "English" to "Английский",
        "Hindi" to "Хинди",
        "Tajik" to "Таджикский",
        "Vietnamese" to "Вьетнамский",
        "Kazakh" to "Казахский",
        "Nepali" to "Непальский",
        "Serbian" to "Сербский",
        "Armenian" to "Армянский",
        "Mandarin" to "Китайский",
        "Turkish" to "Турецкий",
        "Kyrgyz" to "Киргизский",
        "Tagalog" to "Тагальский"
    )

    val workers = listOf(
        WorkerProfile(
            id = 1,
            nameEn = "Jasur Karimov",
            nameRu = "Жасур Каримов",
            countryCode = "UZ",
            profession = "Welder",
            expYears = 7,
            salaryRub = 95000,
            skills = listOf("MIG welding", "Pipe fitting", "Blueprint reading"),
            languages = listOf("Russian", "Uzbek"),
            verified = true,
            bioEn = "7 years pipeline & structural welding experience. Worked on Gazprom projects. All certificates verified.",
            bioRu = "7 лет опыта в сварке трубопроводов и металлоконструкций. Работал на проектах Газпрома. Все сертификаты подтверждены.",
            activeViewers = 17,
            cityEn = "Moscow",
            cityRu = "Москва",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 2,
            nameEn = "Ravi Sharma",
            nameRu = "Рави Шарма",
            countryCode = "IN",
            profession = "Cook",
            expYears = 5,
            salaryRub = 70000,
            skills = listOf("Indian cuisine", "Bakery", "HACCP"),
            languages = listOf("English", "Hindi"),
            verified = true,
            bioEn = "Tandoor and continental specialist. Worked in 5-star hotels in Delhi and Dubai.",
            bioRu = "Специалист по тандыру и европейской кухне. Работал в 5-звёздочных отелях Дели и Дубая.",
            activeViewers = 22,
            cityEn = "Saint Petersburg",
            cityRu = "Санкт-Петербург",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 3,
            nameEn = "Mukhammad Dzhalilov",
            nameRu = "Мухаммад Джалилов",
            countryCode = "TJ",
            profession = "Driver",
            expYears = 9,
            salaryRub = 80000,
            skills = listOf("Category C+E", "Long-haul", "Mechanics"),
            languages = listOf("Russian", "Tajik"),
            verified = true,
            bioEn = "9 years long-haul trucking across Russia and CIS. Clean license, no accidents.",
            bioRu = "9 лет опыта на дальних рейсах по России и СНГ. Чистые права, без ДТП.",
            activeViewers = 19,
            cityEn = "Moscow",
            cityRu = "Москва",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 4,
            nameEn = "Nguyen Pham",
            nameRu = "Нгуен Фам",
            countryCode = "VN",
            profession = "Electrician",
            expYears = 4,
            salaryRub = 75000,
            skills = listOf("Industrial wiring", "PLC", "Safety codes"),
            languages = listOf("English", "Vietnamese"),
            verified = false,
            bioEn = "Industrial electrician with factory installation experience. Certification in progress.",
            bioRu = "Промышленный электрик с опытом монтажа на заводах. Сертификация в процессе.",
            activeViewers = 9,
            cityEn = "Kaluga",
            cityRu = "Калуга",
            responseSpeedEn = "Ready for interview",
            responseSpeedRu = "Готов к собеседованию"
        ),
        WorkerProfile(
            id = 5,
            nameEn = "Aizhan Nurlan",
            nameRu = "Айжан Нурлан",
            countryCode = "KZ",
            profession = "Nurse",
            expYears = 6,
            salaryRub = 65000,
            skills = listOf("ICU care", "First aid", "Phlebotomy"),
            languages = listOf("Russian", "Kazakh", "English"),
            verified = true,
            bioEn = "Registered nurse, 6 years ICU experience. Russian language fluent. Nursing degree verified.",
            bioRu = "Дипломированная медсестра, 6 лет в реанимации. Свободный русский язык. Диплом подтверждён.",
            activeViewers = 15,
            cityEn = "Kazan",
            cityRu = "Казань",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 6,
            nameEn = "Sujan Rai",
            nameRu = "Суджан Рай",
            countryCode = "NP",
            profession = "Construction Worker",
            expYears = 8,
            salaryRub = 60000,
            skills = listOf("Masonry", "Formwork", "Scaffolding"),
            languages = listOf("Hindi", "Nepali"),
            verified = true,
            bioEn = "8 years high-rise construction in Gulf countries. Hard worker, team lead experience.",
            bioRu = "8 лет на высотном строительстве в странах Персидского залива. Опыт бригадира.",
            activeViewers = 28,
            cityEn = "Moscow",
            cityRu = "Москва",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 7,
            nameEn = "Elena Popovic",
            nameRu = "Елена Попович",
            countryCode = "RS",
            profession = "Housekeeper",
            expYears = 5,
            salaryRub = 55000,
            skills = listOf("Hotel housekeeping", "Laundry", "Management"),
            languages = listOf("English", "Serbian", "Russian"),
            verified = true,
            bioEn = "5-star hotel housekeeping supervisor. Detail-oriented, speaks conversational Russian.",
            bioRu = "Старшая горничная в 5-звёздочном отеле. Внимательна к деталям, разговорный русский.",
            activeViewers = 12,
            cityEn = "Sochi",
            cityRu = "Сочи",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 8,
            nameEn = "Artur Grigoryan",
            nameRu = "Артур Григорян",
            countryCode = "AM",
            profession = "Mechanic",
            expYears = 10,
            salaryRub = 90000,
            skills = listOf("Diesel engines", "Hydraulics", "Welding"),
            languages = listOf("Russian", "Armenian"),
            verified = true,
            bioEn = "Heavy-duty diesel mechanic, 10 years with construction & mining equipment.",
            bioRu = "Механик по тяжёлым дизелям, 10 лет опыта со строительной и горной техникой.",
            activeViewers = 16,
            cityEn = "Yekaterinburg",
            cityRu = "Екатеринбург",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 9,
            nameEn = "Mei Lin",
            nameRu = "Мэй Линь",
            countryCode = "CN",
            profession = "Warehouse Worker",
            expYears = 3,
            salaryRub = 50000,
            skills = listOf("Forklift", "Inventory", "Packing"),
            languages = listOf("English", "Mandarin"),
            verified = false,
            bioEn = "Forklift certified, fast order picker. E-commerce warehouse experience.",
            bioRu = "Удостоверение на погрузчик, быстрая сборка заказов. Опыт на складах маркетплейсов.",
            activeViewers = 11,
            cityEn = "Novosibirsk",
            cityRu = "Новосибирск",
            responseSpeedEn = "Available immediately",
            responseSpeedRu = "Готова приступить сразу"
        ),
        WorkerProfile(
            id = 10,
            nameEn = "Kemal Yildiz",
            nameRu = "Кемаль Йылдыз",
            countryCode = "TR",
            profession = "Mason",
            expYears = 12,
            salaryRub = 85000,
            skills = listOf("Bricklaying", "Tiling", "Plastering"),
            languages = listOf("Turkish", "Russian"),
            verified = true,
            bioEn = "Master mason, 12 years. Specialist in decorative stonework and restoration.",
            bioRu = "Мастер-каменщик, стаж 12 лет. Специалист по декоративной каменной кладке и реставрации.",
            activeViewers = 14,
            cityEn = "Moscow",
            cityRu = "Москва",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 11,
            nameEn = "Daler Akhmedov",
            nameRu = "Далер Ахмедов",
            countryCode = "KG",
            profession = "Farmer",
            expYears = 6,
            salaryRub = 45000,
            skills = listOf("Greenhouse", "Livestock", "Tractor"),
            languages = listOf("Russian", "Kyrgyz"),
            verified = true,
            bioEn = "Greenhouse vegetable farming and livestock. Tractor & machinery licensed.",
            bioRu = "Выращивание овощей в теплицах и животноводство. Права тракториста-машиниста.",
            activeViewers = 8,
            cityEn = "Krasnodar",
            cityRu = "Краснодар",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        ),
        WorkerProfile(
            id = 12,
            nameEn = "Juan Cruz",
            nameRu = "Хуан Крус",
            countryCode = "PH",
            profession = "IT Support",
            expYears = 4,
            salaryRub = 100000,
            skills = listOf("Networking", "Helpdesk", "Linux"),
            languages = listOf("English", "Tagalog"),
            verified = true,
            bioEn = "IT support specialist, CCNA certified. Remote or on-site. English fluent.",
            bioRu = "Специалист IT-поддержки, сертификат CCNA. Удалённо или в офисе. Свободный английский.",
            activeViewers = 21,
            cityEn = "Moscow",
            cityRu = "Москва",
            responseSpeedEn = "Replies within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня"
        )
    )

    val vacancies = listOf(
        JobVacancy(
            id = 101,
            titleEn = "MIG/MAG Pipeline Welder (Shift / Rotation)",
            titleRu = "Сварщик MIG/MAG на трубопроводы (вахта)",
            category = "Welder",
            salaryMinRub = 115000,
            salaryMaxRub = 145000,
            employerEn = "OOO StroyEnergoMontazh",
            employerRu = "ООО «СтройЭнергоМонтаж»",
            verifiedEmployer = true,
            rating = 4.8,
            reviewCount = 34,
            cityEn = "Moscow · Kurskaya",
            cityRu = "Москва · Курская",
            visaCountries = listOf("UZ", "IN", "TJ", "KZ"),
            experienceEn = "Exp. 1–3 yrs",
            experienceRu = "Опыт 1–3 года",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Housing & Work Visa included",
            perkRu = "Жильё и виза включены",
            postedEn = "Posted today",
            postedRu = "Опубликовано сегодня",
            activeViewers = 24,
            descEn = "Welding industrial gas & heating steel pipelines in Moscow region. Official employment under Labor Code RF, free dormitory, PPE, hot meals, and MigraStaff visa/patent legal clearance.",
            descRu = "Сварка промышленных газо- и теплопроводов в Московской области. Официальное оформление по ТК РФ, бесплатное общежитие, спецодежда, питание и полное оформление документов.",
            hrPhone = "+7 495 901-12-01",
            hrEmail = "hr-welding@migrastaff.ru"
        ),
        JobVacancy(
            id = 102,
            titleEn = "QR-Code Warehouse Picker / Packer",
            titleRu = "Сканировщик QR-кодов / Пикер на склад",
            category = "Warehouse Worker",
            salaryMinRub = 100000,
            salaryMaxRub = 115000,
            employerEn = "OOO Logistika Market Express",
            employerRu = "ООО «Логистика Маркет Экспресс»",
            verifiedEmployer = true,
            rating = 4.6,
            reviewCount = 52,
            cityEn = "Moscow · Domodedovo",
            cityRu = "Москва · Домодедово",
            visaCountries = listOf("UZ", "TJ", "KG", "IN", "CN"),
            experienceEn = "Without experience",
            experienceRu = "Без опыта",
            payScheduleEn = "Payments: Once a week",
            payScheduleRu = "Выплаты: раз в неделю",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Available without resume",
            perkRu = "Доступно без резюме",
            postedEn = "Posted yesterday",
            postedRu = "Опубликовано вчера",
            activeViewers = 17,
            descEn = "Order picking and QR barcode scanning at a heated A-class e-commerce hub. Free corporate shuttle bus, weekly payouts, full onboarding for foreign citizens.",
            descRu = "Сборка заказов и сканирование QR-кодов на тёплом складе класса А. Бесплатный корпоративный транспорт, еженедельные выплаты, помощь с регистрацией.",
            hrPhone = "+7 495 901-22-14",
            hrEmail = "warehouse@migrastaff.ru"
        ),
        JobVacancy(
            id = 103,
            titleEn = "Tandoor & Continental Hotel Chef",
            titleRu = "Повар тандыра и европейской кухни",
            category = "Cook",
            salaryMinRub = 85000,
            salaryMaxRub = 110000,
            employerEn = "Neva Grand Hospitality Group",
            employerRu = "ГК «Нева Гранд Отель»",
            verifiedEmployer = true,
            rating = 4.9,
            reviewCount = 19,
            cityEn = "Saint Petersburg",
            cityRu = "Санкт-Петербург",
            visaCountries = listOf("IN", "NP", "TR", "UZ"),
            experienceEn = "Exp. 1–3 yrs",
            experienceRu = "Опыт 1–3 года",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Work Quota + Meals free",
            perkRu = "Квота на визу + питание",
            postedEn = "Posted today",
            postedRu = "Опубликовано сегодня",
            activeViewers = 22,
            descEn = "4-star boutique hotel kitchen in central Saint Petersburg. Looking for an experienced chef fluent in Indian/Asian and European breakfast menus. Invitation letter & work permit sponsored.",
            descRu = "Кухня 4-звёздочного отеля в центре Санкт-Петербурга. Ищем повара со знанием индийской/азиатской и европейской кухни. Оформляем приглашение и разрешение на работу.",
            hrPhone = "+7 812 640-31-08",
            hrEmail = "kitchen@migrastaff.ru"
        ),
        JobVacancy(
            id = 104,
            titleEn = "Long-Haul Truck Driver (Category C+E)",
            titleRu = "Водитель-дальнобойщик (категория C+E)",
            category = "Driver",
            salaryMinRub = 135000,
            salaryMaxRub = 180000,
            employerEn = "OOO EvroTrans Logistics RF",
            employerRu = "ООО «ЕвроТранс Логистика РФ»",
            verifiedEmployer = true,
            rating = 4.7,
            reviewCount = 41,
            cityEn = "Moscow · Sheremetyevo",
            cityRu = "Москва · Шереметьево",
            visaCountries = listOf("TJ", "UZ", "KZ", "AM", "KG"),
            experienceEn = "Exp. 3+ yrs",
            experienceRu = "Опыт от 3 лет",
            payScheduleEn = "Payments: Per trip + bonus",
            payScheduleRu = "Выплаты: за рейс + премия",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "New Volvo / KAMAZ fleet",
            perkRu = "Новые тягачи Volvo / КАМАЗ",
            postedEn = "Posted today",
            postedRu = "Опубликовано сегодня",
            activeViewers = 19,
            descEn = "Intercity freight routes Moscow–Kazan–Yekaterinburg. Modern fleet serviced at official dealers, paid per diem & fuel cards, official employment.",
            descRu = "Междугородние грузоперевозки Москва–Казань–Екатеринбург. Новый автопарк с сервисом у дилеров, суточные и топливные карты, официальное оформление.",
            hrPhone = "+7 495 901-44-09",
            hrEmail = "fleet@migrastaff.ru"
        ),
        JobVacancy(
            id = 105,
            titleEn = "High-Rise Construction Team & Formwork",
            titleRu = "Монолитчик / Строитель на высотный объект",
            category = "Construction Worker",
            salaryMinRub = 95000,
            salaryMaxRub = 130000,
            employerEn = "AO MonolitGrad Stroy",
            employerRu = "АО «МонолитГрад Строй»",
            verifiedEmployer = true,
            rating = 4.5,
            reviewCount = 67,
            cityEn = "Moscow · Moscow City",
            cityRu = "Москва · Сити",
            visaCountries = listOf("IN", "NP", "UZ", "TJ", "VN"),
            experienceEn = "Exp. 1+ yr",
            experienceRu = "Опыт от 1 года",
            payScheduleEn = "Payments: Every 2 weeks",
            payScheduleRu = "Выплаты: каждые 2 недели",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Visa Quota + Dormitory",
            perkRu = "Квота на визу + общежитие",
            postedEn = "Posted yesterday",
            postedRu = "Опубликовано вчера",
            activeViewers = 31,
            descEn = "Formwork, rebar tying, and concrete pouring for residential towers. Brigade positions open with English/Hindi translator on site and full DMS medical insurance.",
            descRu = "Опалубка, вязка арматуры и бетонные работы на жилых башнях. Бригады обеспечиваются переводчиком, ДМС, общежитием и визовой поддержкой.",
            hrPhone = "+7 495 901-55-30",
            hrEmail = "build@migrastaff.ru"
        ),
        JobVacancy(
            id = 106,
            titleEn = "Industrial Plant Electrician (PLC & 380V)",
            titleRu = "Промышленный электрик (ПЛК и 380В)",
            category = "Electrician",
            salaryMinRub = 95000,
            salaryMaxRub = 120000,
            employerEn = "AO Kaluga Industrial Park",
            employerRu = "АО «Индустриальный парк Калуга»",
            verifiedEmployer = true,
            rating = 4.7,
            reviewCount = 15,
            cityEn = "Kaluga",
            cityRu = "Калуга",
            visaCountries = listOf("VN", "IN", "RS", "UZ"),
            experienceEn = "Exp. 2+ yrs",
            experienceRu = "Опыт от 2 лет",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Fast interview",
            responseSpeedRu = "Быстрое собеседование",
            perkEn = "Paid Relocation + Housing",
            perkRu = "Релокация + квартира",
            postedEn = "Posted 2 days ago",
            postedRu = "2 дня назад",
            activeViewers = 13,
            descEn = "Maintenance and wiring of automated manufacturing lines, control cabinets, and 380V switchgear. Technical English accepted; corporate apartment provided.",
            descRu = "Обслуживание и монтаж автоматических производственных линий, шкафов управления и сетей 380В. Предоставляется корпоративное жильё.",
            hrPhone = "+7 484 250-19-82",
            hrEmail = "electric@migrastaff.ru"
        ),
        JobVacancy(
            id = 107,
            titleEn = "ICU & Surgical Ward Nurse",
            titleRu = "Медсестра / Медбрат в отделение ОРИТ",
            category = "Nurse",
            salaryMinRub = 80000,
            salaryMaxRub = 105000,
            employerEn = "Kazan International Medical Center",
            employerRu = "Международный медцентр «Казань»",
            verifiedEmployer = true,
            rating = 4.9,
            reviewCount = 28,
            cityEn = "Kazan",
            cityRu = "Казань",
            visaCountries = listOf("KZ", "PH", "IN", "AM"),
            experienceEn = "Exp. 2+ yrs",
            experienceRu = "Опыт от 2 лет",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Medical Credential Help",
            perkRu = "Помощь с нострификацией",
            postedEn = "Posted today",
            postedRu = "Опубликовано сегодня",
            activeViewers = 15,
            descEn = "Modern private clinic in Kazan seeks caring ICU/post-op nurses. Full support with diploma nostrification in Russia, Russian language buddy program, and housing.",
            descRu = "Современная клиника в Казани приглашает медсестёр/медбратьев в реанимацию. Помогаем с признанием диплома в РФ и предоставляем жильё.",
            hrPhone = "+7 843 290-77-11",
            hrEmail = "medhr@migrastaff.ru"
        ),
        JobVacancy(
            id = 108,
            titleEn = "5-Star Resort Housekeeping Supervisor",
            titleRu = "Старшая горничная в курортный отель 5★",
            category = "Housekeeper",
            salaryMinRub = 65000,
            salaryMaxRub = 82000,
            employerEn = "Sochi Azure Riviera Resort",
            employerRu = "Курорт «Лазурная Ривьера Сочи»",
            verifiedEmployer = true,
            rating = 4.8,
            reviewCount = 23,
            cityEn = "Sochi",
            cityRu = "Сочи",
            visaCountries = listOf("RS", "PH", "KG", "UZ"),
            experienceEn = "Exp. 1+ yr",
            experienceRu = "Опыт от 1 года",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Seafront Staff Residence",
            perkRu = "Жильё у моря + питание",
            postedEn = "Posted yesterday",
            postedRu = "Опубликовано вчера",
            activeViewers = 18,
            descEn = "Lead a housekeeping floor team of 8 at a Black Sea 5-star resort. Staff residence, 3 meals daily, uniform, and work permit fees covered by the hotel.",
            descRu = "Руководство сменой горничных в 5-звёздочном отеле в Сочи. Проживание, трёхразовое питание, форма и оформление за счёт работодателя.",
            hrPhone = "+7 862 240-11-90",
            hrEmail = "sochi-hotel@migrastaff.ru"
        ),
        JobVacancy(
            id = 109,
            titleEn = "Heavy Diesel & Hydraulics Mechanic",
            titleRu = "Механик по дизельной и спецтехнике",
            category = "Mechanic",
            salaryMinRub = 110000,
            salaryMaxRub = 140000,
            employerEn = "UralMining Heavy Service",
            employerRu = "«УралМайнинг Сервис»",
            verifiedEmployer = true,
            rating = 4.7,
            reviewCount = 29,
            cityEn = "Yekaterinburg",
            cityRu = "Екатеринбург",
            visaCountries = listOf("AM", "KZ", "UZ", "TR"),
            experienceEn = "Exp. 3+ yrs",
            experienceRu = "Опыт от 3 лет",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Warm Hangar + Bonus",
            perkRu = "Тёплый бокс + премия",
            postedEn = "Posted 2 days ago",
            postedRu = "2 дня назад",
            activeViewers = 16,
            descEn = "Diagnostics and overhaul of excavators, loaders, and heavy diesel generators in heated service bays. High base salary plus per-job bonuses.",
            descRu = "Диагностика и ремонт экскаваторов, погрузчиков и дизель-генераторов в тёплых боксах. Высокий оклад плюс сдельная премия.",
            hrPhone = "+7 343 288-09-45",
            hrEmail = "diesel@migrastaff.ru"
        ),
        JobVacancy(
            id = 110,
            titleEn = "Stone & Brick Facade Master Mason",
            titleRu = "Каменщик-облицовщик (фасады и кладка)",
            category = "Mason",
            salaryMinRub = 105000,
            salaryMaxRub = 135000,
            employerEn = "OOO Stolitsa StoneBuild",
            employerRu = "ООО «Столица СтоунБилд»",
            verifiedEmployer = true,
            rating = 4.6,
            reviewCount = 31,
            cityEn = "Moscow · Sokolniki",
            cityRu = "Москва · Сокольники",
            visaCountries = listOf("TR", "UZ", "TJ", "NP"),
            experienceEn = "Exp. 2+ yrs",
            experienceRu = "Опыт от 2 лет",
            payScheduleEn = "Payments: Once a week",
            payScheduleRu = "Выплаты: раз в неделю",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "Tools & Visa Provided",
            perkRu = "Инструмент и виза от фирмы",
            postedEn = "Posted yesterday",
            postedRu = "Опубликовано вчера",
            activeViewers = 14,
            descEn = "Decorative stone cladding, clinker brick facade laying, and interior plaster finishing on premium Moscow residential projects.",
            descRu = "Облицовка декоративным камнем, клинкерная кладка фасадов и отделочные работы на премиальных жилых комплексах Москвы.",
            hrPhone = "+7 495 901-88-20",
            hrEmail = "masonry@migrastaff.ru"
        ),
        JobVacancy(
            id = 111,
            titleEn = "Greenhouse Agro & Tractor Specialist",
            titleRu = "Специалист тепличного комплекса / Тракторист",
            category = "Farmer",
            salaryMinRub = 60000,
            salaryMaxRub = 78000,
            employerEn = "Kuban GreenTech Agro",
            employerRu = "Агрохолдинг «Кубань ГринТех»",
            verifiedEmployer = false,
            rating = 4.4,
            reviewCount = 12,
            cityEn = "Krasnodar",
            cityRu = "Краснодар",
            visaCountries = listOf("KG", "UZ", "NP", "VN"),
            experienceEn = "Without experience",
            experienceRu = "Без опыта",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Available without resume",
            responseSpeedRu = "Доступно без резюме",
            perkEn = "Free House + Garden",
            perkRu = "Бесплатный дом на участке",
            postedEn = "Posted 3 days ago",
            postedRu = "3 дня назад",
            activeViewers = 11,
            descEn = "Year-round automated hydroponic greenhouses in sunny Krasnodar region. Free cottage accommodation for workers and families.",
            descRu = "Круглогодичные автоматизированные теплицы в Краснодарском крае. Бесплатное проживание в корпоративном коттедже.",
            hrPhone = "+7 861 210-33-17",
            hrEmail = "agro@migrastaff.ru"
        ),
        JobVacancy(
            id = 112,
            titleEn = "Bilingual IT Support & Network Engineer",
            titleRu = "Инженер IT-поддержки (English / Linux)",
            category = "IT Support",
            salaryMinRub = 120000,
            salaryMaxRub = 155000,
            employerEn = "FinBridge Global Tech",
            employerRu = "«ФинБридж Глобал Тех»",
            verifiedEmployer = true,
            rating = 4.9,
            reviewCount = 27,
            cityEn = "Moscow · Hybrid",
            cityRu = "Москва · Гибрид",
            visaCountries = listOf("PH", "IN", "RS", "AM"),
            experienceEn = "Exp. 1–3 yrs",
            experienceRu = "Опыт 1–3 года",
            payScheduleEn = "Payments: Twice a month",
            payScheduleRu = "Выплаты: 2 раза в месяц",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Отвечает в течение 1 дня",
            perkEn = "HQS / IT Visa Fast-Track",
            perkRu = "IT-виза без квот в РФ",
            postedEn = "Posted today",
            postedRu = "Опубликовано сегодня",
            activeViewers = 26,
            descEn = "L2 Linux/Network helpdesk supporting international logistics offices. Russian not mandatory — fluent English required. Eligible for IT specialist visa exemption in Russia.",
            descRu = "Поддержка L2 (Linux и сети) для международных офисов. Достаточно свободного английского языка. Оформление льготной IT-визы в РФ.",
            hrPhone = "+7 495 901-77-99",
            hrEmail = "it-jobs@migrastaff.ru"
        )
    )

    val fxCurrencies = listOf(
        FxCurrency("RUB", "🇷🇺", "Russian Ruble", "Российский рубль", 1.0),
        FxCurrency("INR", "🇮🇳", "Indian Rupee", "Индийская рупия", 0.935),
        FxCurrency("BDT", "🇧🇩", "Bangladeshi Taka", "Бангладешская така", 1.31),
        FxCurrency("PKR", "🇵🇰", "Pakistani Rupee", "Пакистанская рупия", 3.04),
        FxCurrency("NPR", "🇳🇵", "Nepalese Rupee", "Непальская рупия", 1.495),
        FxCurrency("UZS", "🇺🇿", "Uzbekistani Som", "Узбекский сум", 139.5),
        FxCurrency("TJS", "🇹🇯", "Tajikistani Somoni", "Таджикский сомони", 0.118),
        FxCurrency("KGS", "🇰🇬", "Kyrgyzstani Som", "Киргизский сом", 0.945),
        FxCurrency("VND", "🇻🇳", "Vietnamese Dong", "Вьетнамский донг", 276.5),
        FxCurrency("PHP", "🇵🇭", "Philippine Peso", "Филиппинское песо", 0.635),
        FxCurrency("KZT", "🇰🇿", "Kazakhstani Tenge", "Казахстанский тенге", 5.42),
        FxCurrency("AMD", "🇦🇲", "Armenian Dram", "Армянский драм", 4.24),
        FxCurrency("RSD", "🇷🇸", "Serbian Dinar", "Сербский динар", 1.19),
        FxCurrency("TRY", "🇹🇷", "Turkish Lira", "Турецкая лира", 0.382),
        FxCurrency("CNY", "🇨🇳", "Chinese Yuan", "Китайский юань", 0.0788),
        FxCurrency("USD", "🇺🇸", "US Dollar", "Доллар США", 0.0109),
        FxCurrency("EUR", "🇪🇺", "Euro", "Евро", 0.0101)
    )

    val fxPresets = listOf(
        FxPreset("RUB", "INR", 10000.0, "10K RUB → INR"),
        FxPreset("RUB", "BDT", 50000.0, "50K RUB → BDT"),
        FxPreset("RUB", "PKR", 25000.0, "25K RUB → PKR"),
        FxPreset("RUB", "NPR", 15000.0, "15K RUB → NPR")
    )

    val cryptoPlans = listOf(
        CryptoPlan(
            key = "pro",
            nameEn = "Worker — Pro",
            nameRu = "Рабочий — Про",
            descEn = "Priority profile for 1 month",
            descRu = "Приоритетный профиль на месяц",
            rubAmount = 499.0,
            popular = true
        ),
        CryptoPlan(
            key = "business",
            nameEn = "Employer — Business",
            nameRu = "Работодатель — Бизнес",
            descEn = "Unlimited unlocks + 10 jobs",
            descRu = "Безлимит контактов и 10 вакансий",
            rubAmount = 2990.0
        ),
        CryptoPlan(
            key = "unlock",
            nameEn = "Contact Pack (10x)",
            nameRu = "Пакет контактов (10 шт.)",
            descEn = "Unlock 10 verified workers",
            descRu = "10 проверенных контактов рабочих",
            rubAmount = 1500.0
        ),
        CryptoPlan(
            key = "custom",
            nameEn = "Custom Invoice",
            nameRu = "Произвольный счёт",
            descEn = "Agency & legal clearance",
            descRu = "Миграционное оформление",
            rubAmount = 5000.0
        )
    )

    val cryptoCoins = listOf(
        CryptoCoin(
            key = "USDT_TRC20",
            symbol = "USDT",
            network = "TRC-20 (Tron)",
            usdRate = 1.0,
            decimals = 2,
            eta = "~1–2 min",
            address = "TQn9Y2khEsLJW1ChVWFMSMeRDow5KcbLSE"
        ),
        CryptoCoin(
            key = "USDT_ERC20",
            symbol = "USDT",
            network = "ERC-20 (Ethereum)",
            usdRate = 1.0,
            decimals = 2,
            eta = "~2–4 min",
            address = "0x71C7656EC7ab88b098defB751B7401B5f6d8976F"
        ),
        CryptoCoin(
            key = "TON",
            symbol = "TON",
            network = "TON Network",
            usdRate = 5.45,
            decimals = 2,
            eta = "~15 sec",
            address = "UQCD39VS5jcptHL8vMjEXrzGaRcCVYto7HUn4bpAOg8xqEBI"
        ),
        CryptoCoin(
            key = "BTC",
            symbol = "BTC",
            network = "Bitcoin (SegWit)",
            usdRate = 89500.0,
            decimals = 6,
            eta = "~10–20 min",
            address = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh"
        ),
        CryptoCoin(
            key = "ETH",
            symbol = "ETH",
            network = "Ethereum Mainnet",
            usdRate = 3150.0,
            decimals = 5,
            eta = "~2–4 min",
            address = "0x71C7656EC7ab88b098defB751B7401B5f6d8976F"
        )
    )

    val teamMembers = listOf(
        TeamMember(
            nameEn = "Albert Garipova",
            nameRu = "Альберт Гарипова",
            roleEn = "Director & Co-Founder",
            roleRu = "Директор и сооснователь",
            initials = "АГ",
            photoRes = R.drawable.team_albert,
            bioEn = "Leads legal workforce clearance, employer partnerships, and migration compliance across the Russian Federation.",
            bioRu = "Руководит правовым сопровождением найма, партнёрствами с работодателями и миграционным комплаенсом в РФ."
        ),
        TeamMember(
            nameEn = "Alexandra Garipova",
            nameRu = "Александра Гарипова",
            roleEn = "Head of International Operations",
            roleRu = "Руководитель международных операций",
            initials = "АГ",
            photoRes = R.drawable.team_alexandra,
            bioEn = "Oversees candidate screening, bilingual onboarding, and partner pipelines across 40+ source countries.",
            bioRu = "Курирует отбор кандидатов, двуязычную поддержку и международные каналы подбора из 40+ стран."
        )
    )

    val infoTopics = listOf(
        InfoTopic(
            key = "help",
            titleEn = "MigraStaff Help Center",
            titleRu = "Центр помощи MigraStaff",
            bulletsEn = listOf(
                "Candidates: Register free, lock your private profile, and message the Director with EN ⇄ RU support.",
                "Employers: Filter verified workers by profession and citizenship, unlock contacts, or pay corporate invoices via Crypto Pay.",
                "Support Desk: Email migrapro@imap.mail.ru or WhatsApp +7 912 850-03-33."
            ),
            bulletsRu = listOf(
                "Кандидатам: Бесплатная регистрация, закрытый профиль и чат с директором с поддержкой EN ⇄ RU.",
                "Работодателям: Поиск проверенных специалистов, открытие контактов и оплата счетов через криптоэквайринг.",
                "Поддержка: Почта migrapro@imap.mail.ru или WhatsApp +7 912 850-03-33."
            )
        ),
        InfoTopic(
            key = "verify",
            titleEn = "Candidate & Document Verification",
            titleRu = "Верификация кандидатов и документов",
            bulletsEn = listOf(
                "Identity Check: Passport / national ID details reviewed against candidate registration.",
                "Work Portfolio: Real job-site photos, trade certificates, and prior experience review.",
                "Employer Clearance: Legal readiness check for Russian work visas, patents, and quota allocation."
            ),
            bulletsRu = listOf(
                "Проверка личности: Сверка паспорта / удостоверения с данными анкеты.",
                "Портфолио работ: Фотографии с объектов, сертификаты и подтверждение опыта.",
                "Миграционная готовность: Проверка оснований для визы, патента и квот в РФ."
            )
        ),
        InfoTopic(
            key = "migration",
            titleEn = "Russian Workforce Migration Guide",
            titleRu = "Миграционный гид по трудоустройству в РФ",
            bulletsEn = listOf(
                "Visa & Work Permit / Patent: Employer invitation letter, quota clearance, and medical insurance (DMS).",
                "Registration on Arrival: Migration card at border control and address registration within statutory deadlines.",
                "Salary & Currency Benchmark: Paid in RUB or USD per contract; live rates in the FX tab."
            ),
            bulletsRu = listOf(
                "Виза, разрешение или патент: Приглашение от работодателя, квота и полис ДМС.",
                "Миграционный учёт: Миграционная карта при въезде и постановка на учёт в установленные сроки.",
                "Зарплата и курс валют: Выплата в рублях или USD по договору; актуальные курсы во вкладке FX."
            )
        ),
        InfoTopic(
            key = "privacy",
            titleEn = "Privacy Policy",
            titleRu = "Политика конфиденциальности",
            bulletsEn = listOf(
                "Locked Profiles: Phone numbers, passports, and private messages are visible only to the owner and authorized Directors.",
                "Data Use: Processed strictly for legal employment matching and migration clearance.",
                "Requests: Write to migrapro@imap.mail.ru to update or remove your profile."
            ),
            bulletsRu = listOf(
                "Закрытые анкеты: Телефоны, паспорта и переписка доступны только владельцу анкеты и уполномоченному директору.",
                "Цели обработки: Подбор персонала и законное миграционное оформление в РФ.",
                "Обращения: Пишите на migrapro@imap.mail.ru для изменения или удаления данных."
            )
        ),
        InfoTopic(
            key = "terms",
            titleEn = "Terms of Service",
            titleRu = "Условия использования",
            bulletsEn = listOf(
                "ООО «МИГРАСТАФФ» (INN: 1800020150, KPP: 772345001) operates MigraStaff in Moscow.",
                "Workers register free; Pro upgrades and Crypto Pay settlements are optional.",
                "Unlocked employer contacts may only be used for lawful recruitment in Russia."
            ),
            bulletsRu = listOf(
                "ООО «МИГРАСТАФФ» (ИНН: 1800020150, КПП: 772345001) осуществляет работу платформы MigraStaff в Москве.",
                "Регистрация работников бесплатна; тарифы Про и криптооплата подключаются по желанию.",
                "Открытые контакты используются исключительно для законного найма персонала в РФ."
            )
        )
    )

    fun defaultRatesPerRub(): Map<String, Double> =
        fxCurrencies.associate { it.code to it.fallbackPerRub }

    fun countryOf(code: String): CountryInfo? = countries.find { it.code == code }

    fun countryName(code: String, lang: AppLang): String {
        val c = countryOf(code) ?: return code
        return if (lang == AppLang.RU) c.nameRu else c.nameEn
    }

    fun profName(profEn: String, lang: AppLang): String =
        if (lang == AppLang.RU) profRu[profEn] ?: profEn else profEn

    fun skillName(skillEn: String, lang: AppLang): String =
        if (lang == AppLang.RU) skillRu[skillEn] ?: skillEn else skillEn

    fun langName(langEn: String, lang: AppLang): String =
        if (lang == AppLang.RU) langRu[langEn] ?: langEn else langEn

    /**
     * Deterministic 21x21 QR-like visual matrix identical to crypto.html renderQrSvg():
     * FNV-1a 32-bit hash with 3 standard 7x7 finder patterns.
     */
    fun generateQrMatrix(seedText: String): Array<BooleanArray> {
        val size = 21
        var h: UInt = 2166136261u
        for (ch in seedText) {
            h = h xor ch.code.toUInt()
            h *= 16777619u
        }

        fun isFinder(r: Int, c: Int): Boolean =
            (r < 7 && c < 7) || (r < 7 && c >= size - 7) || (r >= size - 7 && c < 7)

        fun finderOn(r: Int, c: Int, r0: Int, c0: Int): Boolean {
            val dr = r - r0
            val dc = c - c0
            return dr == 0 || dr == 6 || dc == 0 || dc == 6 || (dr in 2..4 && dc in 2..4)
        }

        return Array(size) { r ->
            BooleanArray(size) { c ->
                when {
                    r < 7 && c < 7 -> finderOn(r, c, 0, 0)
                    r < 7 && c >= size - 7 -> finderOn(r, c, 0, size - 7)
                    r >= size - 7 && c < 7 -> finderOn(r, c, size - 7, 0)
                    (r == 7 && (c < 8 || c >= size - 8)) ||
                        (c == 7 && (r < 8 || r >= size - 8)) -> false
                    !isFinder(r, c) -> {
                        h = h xor (r * 31 + c * 17 + 7).toUInt()
                        h *= 16777619u
                        ((h shr 24) and 1u) == 1u
                    }
                    else -> false
                }
            }
        }
    }
}
