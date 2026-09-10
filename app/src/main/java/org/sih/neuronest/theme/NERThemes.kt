package org.sih.neuronest.theme

import androidx.compose.ui.graphics.Color

enum class NERRegion(
    val stateName: String,
    val culturalSymbol: String,
    val primaryColor: Color,
    val accentColor: Color,
    val backgroundGreeting: String
) {
    ASSAM(
        stateName = "Assam",
        culturalSymbol = "Bihu Dhol & One-Horned Rhino",
        primaryColor = AssamBihuGold,
        accentColor = PrimaryTeal,
        backgroundGreeting = "Nomoskar! (নমস্কাৰ)"
    ),
    MANIPUR(
        stateName = "Manipur",
        culturalSymbol = "Sangai Deer & Kangla Palace",
        primaryColor = ManipurSangaiGreen,
        accentColor = SecondaryWarmGold,
        backgroundGreeting = "Khurumjari! (খুরুমজরি)"
    ),
    NAGALAND(
        stateName = "Nagaland",
        culturalSymbol = "Great Hornbill & Traditional Weaving",
        primaryColor = NagalandHornbillRed,
        accentColor = AssamBihuGold,
        backgroundGreeting = "Welcome to Nagaland!"
    ),
    MEGHALAYA(
        stateName = "Meghalaya",
        culturalSymbol = "Living Root Bridges & Cascading Waterfalls",
        primaryColor = MeghalayaRootBlue,
        accentColor = MizoBambooEmerald,
        backgroundGreeting = "Khublei! (Blessings)"
    ),
    MIZORAM(
        stateName = "Mizoram",
        culturalSymbol = "Cheraw Bamboo Dance & Emerald Hills",
        primaryColor = MizoBambooEmerald,
        accentColor = AssamBihuGold,
        backgroundGreeting = "Chibai! (Hi)"
    ),
    TRIPURA(
        stateName = "Tripura",
        culturalSymbol = "Ujjayanta Palace & Tripura Tea Gardens",
        primaryColor = TripuraTeaAmber,
        accentColor = PrimaryTeal,
        backgroundGreeting = "Nalengma! (Welcome)"
    ),
    ARUNACHAL_PRADESH(
        stateName = "Arunachal Pradesh",
        culturalSymbol = "Land of the Dawn-Lit Mountains & Tawang",
        primaryColor = GamePurpleAccent,
        accentColor = AssamBihuGold,
        backgroundGreeting = "Tashi Delek!"
    ),
    SIKKIM(
        stateName = "Sikkim",
        culturalSymbol = "Kanchenjunga Peak & Prayer Wheels",
        primaryColor = GameInfoIndigo,
        accentColor = MizoBambooEmerald,
        backgroundGreeting = "Namaste & Tashi Delek!"
    )
}
