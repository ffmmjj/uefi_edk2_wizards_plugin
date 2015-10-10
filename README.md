# UEFI EDK2 Wizards plugin for Eclipse
This project is an Eclipse plugin that aims to provide a set of Eclipse wizards on top of Eclipse CDT to ease the development of EDK2-based UEFI modules.

This is a work in progress, so feel free to suggest new features and let me know if you find any bugs while using it :)

# A tour

[![UEFI EDK2 Wizards plugin tour](http://img.youtube.com/vi/ELqV1xDdPkw/0.jpg)](https://www.youtube.com/watch?v=ELqV1xDdPkw)

# Eclipse Update site
 The zipped update site for this Eclipse plugin can be downloaded from [here](https://dl.dropboxusercontent.com/u/4482867/edk2tools_update_site.zip).
 Download this file and add it as a local update site to your Eclipse CDT instance
 to install this plugin(You might have to uncheck the option *Group items by category* in the **Install new software** dialog)
 
# Known issues
   - Sometimes, the source indexer doesn't immediately gets everything right for large projects(which means that autocomplete and syntax highlighting won't work, for instance). A quick fix around it is to rebuild the index by right-clicking the project and selecting *Index* --> *Rebuild*;

# Current status
  - Existing EDK2 Module project wizard:
    - ~~Import .inf file;~~
    - ~~Populate project sources in Project Explorer view;~~
    - ~~Add include paths extracted from the .dec files referenced in the module's .inf file;~~
    - ~~Add custom workspace path to the project wizard;~~
    - Add build steps to the new project;
  - New EDK2 Module project wizard:
    - Initial setup page(Module name, module type, referenced packages(.dec), consumed library classes, protocols and GUIDs);
    - Platforms page(which .dsc files will included the new module);
    - New library class implementation page;
    - UEFI Driver settings page(driver type, depex, etc);
