# RecipeDumper

[![Modrinth Badge](https://img.shields.io/badge/Available-RecipeDumper?style=for-the-badge&logo=modrinth&label=Modrinth&labelColor=hsl(221%2C%2039%25%2C%2011%25)&color=1a202c&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Frecipedumper)](https://modrinth.com/mod/recipedumper)

A handy tool to dump recipes from Minecraft.

## Usage

- Commands:
  - `/dump recipes`: Dump all recipes.
  - `/dump tags items`: Dump all item tags.
  - `/dump tags blocks`: Dump all block tags.
  - `/dump tags fluids`: Dump all fluid tags.
  - `/dump all`: Dump all tags.

Outputs will be in `[game directory]/dumps/`.

## Future Plans

In the future (as I am coming back to this), I plan to:

- [ ] Port to Architectury
- [ ] Support at least 1.16 through 1.20.4 (and 1.21)
- [ ] Make a web service to view recipes and tags (including recipes for mods like Create and Mekanism), and calculate base requirements
- [ ] Have the web service include pre-dumped files for modpacks (listed below)
- [ ] Make a compressed binary representation for large amounts of data

### Future Plans - Modpacks

- Vanilla Minecraft - 1.16 to 1.20 (not a modpack)
- Create: Mekanized
- Create: Above & Beyond
- Enigmatica 6: Expert
- Create: Astral
- Greate: Beyond the Horizon
- ... and more!

### Future Plans - Binary Representation

The structure will be as follows:

```txt
/*
enum MaterialType {
    // These are shorts

    Item = 0,
    Block = 1,
    Fluid = 2,
    
    // This will only be present if Mekanism is found
    Gas = 3,
}
*/

// Header
(in ASCII hex) RDMP\0 // Magic
(shorts) 1 0 0\0 // Version

// Item Index
IIDX\0
    // Repeated
    (int32) [index in array]\0
    (string) [item id]\0
IEND\0

// Fluid index
FIDX\0
    // Repeated
    (int32) [index in array]\0
    (string) [fluid id]\0
FEND\0

// Gas index (Mekanism only)
GIDX\0
    // Repeated
    (int32) [index in array]\0
    (string) [gas id]\0
GEND\0

// Tag Index
TIDX\0
    // Repeated
    ELEM\0
        (int32) [index in array]\0
        (string) [tag id]\0
        (enum: MaterialType [as short]) [tag type]\0

        // Tag members
        (list<int32>) [material indexes in their array]\0
    EEND\0
IEND\0

// Recipe Index
RIDX\0
    // Repeated
    ELEM\0
        (int32) [index in array]\0

        // Inputs
        SECT\0
            RIN\0
                (enum: MaterialType [as short]) [material type]\0
                (int32) [material index in its array]\0
            NEND\0
        SEND\0

        // Outputs
        SECT\0
            ROUT\0
                (enum: MaterialType [as short]) [material type]\0
                (int32) [material index in its array]\0
            OEND\0
        SEND\0
    EEND\0
REND\0

// Recipe Type Map
RMAP\0
    // Repeated
    ELEM\0
        (string) [recipe type id]\0 // The recipe type is the key in the map

        // Values
        (list<int32>) [recipe indexes in recipes array]\0
    EEND\0
MEND\0

EOF
```

All strings are null-terminated and all numbers are in little endian format.

