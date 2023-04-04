# Proximity Chat

A Omegle-like proximity-based Android chatting app that matches people with closest strangers.

- Authors: Martin Čorovčák, Daniel Josef
- Platform: Android
- Current version: 1.0
- Language: Kotlin
- Architecture Stack: 
  - Model-View-ViewModel using Jetpack Compose and Firebase
  - Frontend: 
    - Jetpack Compose toolkit for building native UI in Kotlin
    - Material 3 UI styling standards
  - Backend:
    - Firebase SDK
      - Realtime Database for chatting functionality
    - Android native location provider API

## Description

The main functionality of the application is based upon a popular chatting website [Omegle.com](https://www.omegle.com), that matches users with different random users, currently in the queue, in a one-to-one chat session based on first-come-first-serve basis. The core difference is that **Proximity Chat** also uses location-based priority queue, that matches a first user to the geographically closest user currently in the queue. These users can chat about anything, use the displayed user distance to find each (or not), and when some of them decides to chat with someone else, the process repeats -> with different user now.

## Screenshots

<p float="left">
  <img src="https://i.imgur.com/DgCpiS8.png" allign="top" width="24%" title="Start Screen">
  <img src="https://i.imgur.com/PLDqvKt.png" allign="top" width="24%" title="Proximity Queue">
  <img src="https://i.imgur.com/fmJA11n.png" allign="top" width="24%" title="Proximity Queue - User Disconnected">
  <img src="https://i.imgur.com/pETT2Oz.png" allign="top" width="24%" title="Chat">
</p>
