# UEFI EDK2 Wizards plugin for Eclipse
This project is an Eclipse plugin that aims to provide a set of Eclipse wizards on top of Eclipse CDT to ease the development of EDK2-based UEFI modules.

# Current status
  - Existing EDK2 Module project wizard:
    - ~~Import .inf file;~~
    - ~~Populate project sources in Project Explorer view;~~
    - ~~Add include paths extracted from the .dec files referenced in the module's .inf file;~~
    - Add custom workspace path to the project wizard;
    - Add build steps to the new project;
  - New EDK2 Module project wizard:
    - Initial setup page(Module name, module type, referenced packages(.dec), consumed library classes, protocols and GUIDs);
    - Platforms page(which .dsc files will included the new module);
    - New library class implementation page;
    - UEFI Driver settings page(driver type, depex, etc);
