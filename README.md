* Android Architecture
This is a project used to show how the Android App should be organized.
  
This project has been developed using Android Studio Bumblebee | 2021.1.1 Patch 1

** Business login
The business login is implemented using Clean Architecture in two different modules:

- Domain: it exposes all the use cases used by the presentation layer of the App
- Data: it implements the repositories defined by the Domain module using the data sources

To force the developers to avoid using any Android SDK reference in the domain layer, the domain 
module is a java/kotlin library (the developer cannot add any Android dependency in this module)

The data module is an Android library and it has full access to the Android SDK and Android 
libraries.

** Presentation logic
The presentation logic is implemented using Jetpack Compose and following the MVVM Architecture:

- Model: the model is implemented in the domain module using Clean Architecture
- View: the view is implemented using Jetpack Compose
- ViewModel: the view model is implemented using the AndroidX library
