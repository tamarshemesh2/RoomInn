![RoomInn](https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/background_readme.png?raw=true)

# RoomInn Application

RoomInnn is a room design app where the user could plan the layout of their room by scanning their
space, creating a floor plan, adding furniture from our database and viewing the final result in VR.

## Preview
<img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_1.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_2.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_3.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_4.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_5.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_6.jpeg?raw=true" width="150" /> <img src="https://github.com/tamarshemesh2/RoomInn/blob/master/RoomInn/app/src/main/res/drawable/screenshot_7.jpeg?raw=true" width="150" />




## Technologies Used
* Android Studio
* Unity- AR Foundation and VR
* Google AR Core
* Firebase Firestore


## Technical Description
The main flow of the app is implemented in Android Studio, where we used two Activities: one for login/registration and one as Main Activity.
The App’s flow and actions are rendered using Fragments together with MVVM.
The user creates a floor plan of their room using Google’s AR-Core implemented in Unity.
Then use gestures to interact the floor plan to add / edit furniture and locate in the room.
At any time the user could view the design in VR, implemented in Unity as well.
We used Firebase to manage user login authentication & database.

## Demo 
[![Watch the video](https://img.youtube.com/vi/Z-GFqyqcpm4/0.jpg)](https://youtu.be/Z-GFqyqcpm4)

## Installation

Clone this repository and import into **Android Studio**

```bash
git clone https://github.com/tamarshemesh2/RoomInn
```
 
Make sure to download:
 * https://drive.google.com/file/d/1yOcSx-tYFnM7UfThUwP4Uafp-51uupYC/view?usp=sharing 
 * https://drive.google.com/file/d/1Y9BhRaYtsAs0-XrdnbwGgj_5EZjJFmc6/view?usp=sharing“

 
Save the folder structure as followed:
```gradle
* RoomInn
|_____RoomInn
|_____tools
|_____UnityProject
```

## Maintainers

This project is mantained by:

* [Tamar Shemesh] https://github.com/tamarShemesh2)
* [Daniella Friedman] https://github.com/daniella97)
* [Natan Adi] https://github.com/NatanAdiGit)
* [Yuval Cohen] https://github.com/yuvalCohen14)

