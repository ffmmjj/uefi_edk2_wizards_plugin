# UEFI EDK2 Wizards plugin for Eclipse
This project is an Eclipse plugin that aims to provide a set of Eclipse wizards on top of Eclipse CDT to ease the development of EDK2-based UEFI modules.

This is a work in progress, so feel free to suggest new features and let me know if you find any bugs while using it :)

# A tour

[![EDK II plugins for Eclipse - Existing module project ](https://img.youtube.com/vi/BgUr8osUEhg/0.jpg)](https://youtu.be/BgUr8osUEhg)

# Eclipse Update site
 The zipped update site for this Eclipse plugin can be downloaded from [here](https://dl.dropboxusercontent.com/u/4482867/edk-tools-plugin.zip).
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
    - Add support for different architectures(currently fixed on X64);
    - Add build steps to the new project;
  - New EDK2 Module project wizard:
    - ~~Initial setup page(Module name, module type, location);~~
    - ~~New library class implementation page;~~
    - ~~UEFI Driver Model toggle page;~~
    - Add packages selection page;
    - Add library classes selection page;
  - INF editor;
  - Module explorer view;
  - Add PCDs to Indexer;
