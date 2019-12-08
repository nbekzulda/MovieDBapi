package com.example.moviedbapi.data.roomCinema

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Cinema::class], version = 2, exportSchema = false)
abstract class CinemaRoomDatabase: RoomDatabase() {
    abstract fun cinemaDao() : CinemaDao

    companion object {
        @Volatile
        private var INSTANCE: CinemaRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CinemaRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CinemaRoomDatabase::class.java,
                    "cinema_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CinemaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
    private class CinemaDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cinemaDao())
                }
            }
        }

        suspend fun populateDatabase(cinemaDao: CinemaDao) {
            cinemaDao.deleteAll()

            var cinema = Cinema(
                1,
                "Chaplin Mega Alma-Ata",
                "г. Алматы, Розыбакиева, 263, ТРЦ MEGA Alma-Ata, 2 этаж",
                "со стороны Розыбакиева",
                "со стороны улицы Сатпаева",
                "+7 747 174 06 07",
                "имеется",
                "/i/files/media/chaplin-mega-almaty.jpg?h=300",
                43.304039,
                76.929475
                )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                2,
                "Сhaplin-mega-silkway",
                "г. Нур-Султан, ТРЦ MEGA Silk Way (территория EXPO), 2 этаж, Chaplin MEGA Silk Way",
                "по пр. Абая пересечение улицы Ауэзова",
                "со стороны проспекта Кабанбай батыра и улицы Акмешит",
                "+7 747 174 00 89",
                "лаунж-бар кинотеатра с авторским меню",
                "/i/files/media/chaplin-mega-silkway--astana.jpg?h=300",
                51.08892234,
                71.40737772
            )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                3,
                "Арман 3D (Азия Парк)",
                "г.Нур-Султан, пр. Кабанбай Батыра, 21 ТРЦ «Азия парк»",
                "Бесплатные наземная парковка и подземный паркинг",
                "со стороны улицы Султанмахмуда Торайгырова",
                "+7 7172 97 87 95",
                "бар",
                "/i/files/media/arman-3d-aziya-park-astana.jpg?h=300",
                51.1280624,
                71.41163344
            )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                4,
                "Kinopark 6 Keruencity Astana",
                "г. Нур-Султан, ТЦ «KeruenCity Astana», 3 эт., Кургальджинское шоссе, 1",
                "две наземные парковки на 250 мест. Бесплатные. Заезд со стороны шоссе Коргалжын",
                "со стороны шоссе Коргалжын",
                "+7 7172 790 999",
                "фуд-корт",
                "/i/files/media/kinopark-6-keruencity-astana.jpg?h=300",
                51.1454644,
                71.41372597
            )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                5,
                "Кинотеатр Arsenal",
                "г. Нур-Султан, ул. Ы. Алтынсарина, 4",
                "подземная парковка на 10 мест. Бесплатная. Заезд со стороны улицы Ыбырая Алтынсарина",
                "со стороны улицы Ыбырая Алтынсарина",
                "8-701-520-59-78",
                "фуд-корт",
                "/i/files/media/arsenal-astana.jpg?h=300",
                51.1275005,
                71.4374077
            )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                6,
                "Kinopark 8 IMAX Saryarka",
                " г. Нур-Султан, пр. Туран, 24, ТЦ «Сары-арка», 3 этаж",
                "Заезд со стороны проспекта Туран",
                "со стороны проспекта Туран",
                "8 (7172) 79–09–99",
                "фуд-корт",
                "/i/files/media/kinopark-8-imax-saryarka.jpg?h=300",
                51.1384015,
                71.409995
            )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                7,
                "Bekmambetov Cinema",
                "г. Алматы пр. Абая, 109, МФК «Globus»",
                "по пр. Абая пересечение улицы Ауэзова",
                "со стороны проспекта Абая",
                "8(727) 356-98-78",
                "имеется",
                "/i/files/media/bekmambetov-cinema.jpg?h=300",
                43.240263,
                76.905654
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                8,
                "Киноцентр Арман (ТРЦ Asia Park)",
                "г. Алматы, пр. Райымбека 514а, уг. ул. Саина, Торгово-развлекательный центр «Asia Park»",
                "наземная, заезд со стороны проспекта Райымбека и улицы Саина",
                "со стороны проспекта Райымбека и со стороны улицы Саина",
                "+7 727 343 51 00, +7 727 343 51 11",
                "имеется",
                "/i/files/media/kinocentr-arman-trc-asia-park.jpg?h=300",
                43.242221,
                76.957644
            )

            cinemaDao.insert(cinema)
            cinema = Cinema (
                9,
                "LUMIERA Cinema",
                "г. Алматы, пр. Абылай хана, 62, «Арбат»",
                "за ЦУМом по улице Алимжанова",
                "со стороны улицы Сейфулина и со стороны улицы Макатаева",
                "+7 727 222 23 23, +7 707 782 82 11",
                "имеется",
                "/i/files/media/kinoteatr-lumiera-almaty-0.jpg?h=300",
                43.262118,
                76.941373
            )

            cinemaDao.insert(cinema)
            cinema = Cinema (
                10,
                "CINEMAX Dostyk Multiplex / Dolby Atmos 3D",
                "г. Алматы, Самал-2, пр. Достык 111, уг. ул. Жолдасбекова, ТРЦ Dostyk Plaza",
                "заезд на парковку со стороны проспекта Достык и со стороны улицы Жолдасбекова",
                "со стороны улицы Сейфулина и со стороны улицы Макатаева",
                "+7 727 222 00 77, +7 727 225 39 01, +7 701 026 73 69",
                "имеется",
                "/i/files/media/cinemax-dostyk-plaza2.jpg?h=300",
                43.233015,
                76.955765


            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                11,
                "Кинотеатр Kinoplexx Sary Arka 3D (г. Алматы)",
                "г. Алматы, ул. Алтынсарина, 24",
                "имется наземная Парковка для посетителей платная — 200 тг",
                "со стороны проспекта Алтынсарина",
                "8 727 277 0038",
                "имеется",
                "/i/files/media/kinoplexx sary arka2019.jpg?h=300",
                43.228496,
                76.857868
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                12,
                "ТТИ «Алатау» 3D (г. Алматы)",
                "г. Алматы, микрорайон Нұркент, 6.",
                "наземная парковка на 50 мест. Бесплатная. Въезд со стороны улицы Бауыржана Момышулы",
                "со стороны улицы Бауыржана Момышулы",
                "8 (727) 398–85–36, 8 (727) 224–89–77",
                "имеется",
                "/i/files/media/tti-alatau-3d.jpg?h=300",
                43.260613,
                76.820057
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                13,
                "Kinopark 11 (Есентай) IMAX (г. Алматы)",
                "г. Алматы, пр. Аль-Фараби, 77/8, ТЦ «Есентай-Молл»",
                "имеется подземная, платная — первые 20 минут - бесплатно; 1 час и каждые последующие часы – по 150 тенге",
                "со стороны Аль-Фараби",
                "+7-701-762-45-11",
                "имеется",
                "/i/files/media/kinopark-11-jesentaj-imax.jpg?h=300",
                43.218290,
                76.927638
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                14,
                "Kinopark 8 Moskva (г. Алматы)",
                "г. Алматы, пр. Абая, уг. Алтынсарина, ТРЦ MOSKVA Metropolitan",
                "имется наземная и подземная . Первые 15 минут бесплатно, час 200 тенге",
                "со стороны проспекта Алтынсарина и проспекта Абая",
                "+7 778 099 09 17, + 7727 331 76 99",
                "имеется",
                "/i/files/media/kinopark-8-moskva.jpg?h=300",
                43.226886,
                76.864135
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                15,
                "Кинотеатр «Цезарь 3D»",
                "г. Алматы, ул. Фурманова, 50, уг. ул. Гоголя",
                "Парковка кинотеатра временно не работает",
                "со стороны проспекта Назарбаева",
                "+7 727 273-63-93",
                "кафе (300 метров от кинотеатра)",
                "/i/files/media/caesar2019.jpg?h=300",
                43.261020,
                76.946446
            )
            cinemaDao.insert(cinema)
        }
    }
}