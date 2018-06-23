# Price Per Unit
A simple Android app that helps you compare prices of similar products

# TODO
- Save items to the database to save states for next time
    - Save when the application is closing (onResume)
    - Restore when it is opened (onPause)
- Append g/kg/l/whatever to the end of size
    - Check if is is currently on there and put it on there if not
    - Strip all [a-z] with regex to get value
- Settings
    - Switch unit/$ to $/unit
        - Round to 4dp for u/$
        - Round to 2dp for $/u
    - Change currently symbol
- Results tile
    - Blue /w white text
    - Shows Item | PPU in a table form (centered to middle)
    - Can select what unit to view results in
        - Most likely place under table in smaller writing