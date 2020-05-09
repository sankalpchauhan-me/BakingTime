# Baking App

- Used Android Architecture Pattern LiveData, ViewModel & Room, to minimize API calls and to survive configuration changes
- Companion App Widget (List of Ingredients can be set using "INGREDIENT" menu button's BottomSheetFragment in Recipe Detail Activity by toggling "Add To Widget" Switch)
- Retry Mechanism, in case first api hit fails, the app will make upto 3 attempts to hit the endpoint.
- Caching Mechanism, provides limited results even when app is offline
- Instrumentation Tests using Mock Objects, Intent Stub, Intent Verification & Idling Resources
- Performed Lint Check

All images used in this project are copyright free and are labelled for reuse by their respective creators

Attributions: 
Recipe Image - Image by OpenClipart-Vectors from Pixabay <a href="https://pixabay.com/users/OpenClipart-Vectors-30363/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=575434">OpenClipart-Vectors</a>

