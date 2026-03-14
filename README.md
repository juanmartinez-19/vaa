# Android Travel Request App

Aplicación Android desarrollada en **Kotlin** que permite a los usuarios solicitar viajes a través de un formulario interactivo y gestionar solicitudes desde distintos roles.

La aplicación implementa una arquitectura moderna basada en **MVVM**, manejo de estado de UI y comunicación con **Firebase** para autenticación y almacenamiento de datos.

---

# Architecture

La aplicación sigue el patrón **MVVM (Model – View – ViewModel)** junto con el **Repository Pattern** para separar responsabilidades y mejorar la mantenibilidad del código.

UI Layer (Fragments / Activities)
↓
ViewModel
↓
Repository
↓
Firebase (Auth + Firestore)

Además se utiliza **Dependency Injection con Hilt** para desacoplar las dependencias entre ViewModels y repositorios.

---

# Tech Stack

* Kotlin
* MVVM Architecture
* LiveData
* Coroutines
* Navigation Component
* RecyclerView
* Hilt (Dependency Injection)
* Firebase Authentication
* Cloud Firestore

---

# Features

### Autenticación y sesión

* Registro e inicio de sesión de usuarios
* Persistencia de sesión
* Manejo de roles de usuario (**USER / ADMIN**)

### Gestión de viajes

* Formulario interactivo para solicitar viajes
* Validación de datos del formulario
* Visualización de historial de viajes
* Gestión de solicitudes desde el panel de administrador

### Arquitectura

* ViewModels para manejo de lógica de negocio
* Repositorios para acceso a datos
* Manejo de estado de UI mediante **UI State pattern**

---

# Screenshots

### Login

![Login](login.jpg)

### Home Usuario

![Home User](home_user.jpg)

### Home Admin

![Home Admin](home_admin.jpg)

### Formulario de viaje

![Form](form_user.jpg)

---

# Future Improvements

* Unit and integration testing
* Offline caching
* Performance optimizations
