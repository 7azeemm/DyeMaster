# DyeMaster
This plugin allows server admins to create custom dyes in Minecraft with hex colors and various customizations.

## Features

- **Customizable item properties**:
  - Name
  - Lore
  - Color
  - Recipe
  - Icon (vanilla Minecraft dyes or custom skull textures)
- **Support for leather armor**: dyes can be applied to all types of leather armor and leather horse armor. (dyes also can be applied to trimed armors)

## Coming soon

- **Gradient Dyes**
- **Dyed Armor Effects**
- **Dynamic Lore**
- **Mob loot**
- **Multi-Versions support**
- **API**

## Images:
![2](https://github.com/user-attachments/assets/a8baef49-eb53-4626-b2de-01388a8586db)
![3](https://github.com/user-attachments/assets/2c94ccb8-8cfc-42a4-9dca-bcf359577946)
![4](https://github.com/user-attachments/assets/8046842c-b396-47df-b977-36fcc203850c)
![1](https://github.com/user-attachments/assets/13b67279-5b79-49b0-9276-e79df9089a9b)


## Notes:

- **Custom skull**: the texture url must be in this format: `https://textures.minecraft.net/texture/[TEXTURE_URL]`
- **Lore**:
  - `[color]`: Replaces this with the chosen dye color.
  - `[color:#RRGGBB`: Use a specific hex color.
  - `&<color_code>`: Use Minecraft formatting codes.
  - `\n`: Inserts a new line.

## Commands

- **/dyes**: Opens the dye creation menu. Only accessible to server admins.

## Permissions

- `dyemaster.gui` - Allows the user to run /dyes command to open dyes menu.
- `dyemaster.dyearmor` - Allows the user to apply custom dye to armor.
- `dyemaster.craftdye` - Allows the user to craft custom dyes.

## Configuration

You can configure the plugin settings in `plugins/DyeMaster/config.yml`. Here are the default settings:

- `DyeNameColor`: **true** (Toggles whether dye name will be colored)
- `ArmorNameColor`: **true** (Toggles whether armor name will be colored)

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request. or contact me.

## Support

For support, open an issue on the [GitHub Issues page](https://github.com/7azeemm/DyeMaster/issues).
or contact me on discord **[#7azem_]()**

## License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/7azeemm/DyeMaster/blob/master/LICENSE) file for details.

**If you're having issues using the plugin, please contact me BEFORE making a review. I *cannot* give support in the review section.**
