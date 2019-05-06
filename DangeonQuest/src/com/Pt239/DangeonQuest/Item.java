package com.Pt239.DangeonQuest;

/**
 * Created by Артем on 24.04.2016.
 */
public class Item {
    public static int
            //NO CHANGES
            //ADD ONLY
            ITEM_SLOT = -1,
            TREASURE = 0,
            FAKE_TREASURE = 1,
            KEY = 2,
            SWORD = 3,
            CROSSBOW = 4,
            ARROW = 5,
            PICK = 6,
            MAGIC_ESSENCE=7,
            PEACE_OF_MAP=8;

    public static String[] ITEM_NAMES = {
            "Сундук",
            "Сундук",
            "Ключ",
            "Меч",
            "Арбалет",
            "Стрела",
            "Кирка",
            "Неведомая магическая хрень",
            "Кусок карты"
    };

        public static String[] ITEMS_DESCRIPTIONS = {
                "Возможно, это именно то, что нам нужно",
                "Возможно, это именно то, что нам нужно",
                "Непонятно - зачем он здесь?",
                "Весьма острый. Им можно убить монстра",
                "Инструкция по использованию прилагается",
                "Только чёрной стрелой можно пробить броню дракона! (Нанесёт 2 урона)",
                "100% алмазная",
                "Излучает тёплый свет.",
                "Кусок бумаги со странными символами"
        } ;

    public  static final int
            ID_TREASURE_BITMAP=R.drawable.sunduk0,
            ID_KEY_BITMAP=R.drawable.key0,
            ID_SWORD_BITMAP=R.drawable.sword0,
            ID_ARROW_BITMAP=R.drawable.arrow0,
            ID_CROSSBOW_BITMAP=R.drawable.crossbow0,
            ID_ESSENCE_BITMAP=R.drawable.essence0,
            ID_PEACE_OF_MAP_BITMAP=R.drawable.scroll0;
}
