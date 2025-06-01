group = "funn.j2k"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.2.0-RC"
    alias(libs.plugins.paperweight)
    alias(libs.plugins.nova)
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.nova)
}

addon {
    name = project.name.replaceFirstChar(Char::uppercase)
    version = project.version.toString()
    main = "funn.j2k.politicsMcAddon.PoliticsMcAddon"
    
    // output directory for the generated addon jar is read from the "outDir" project property (-PoutDir="...")
    val outDir = project.findProperty("outDir")
    if (outDir is String)
        destination.set(File(outDir))
}
