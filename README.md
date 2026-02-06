# Simulador Táctico

Aplicación web completa para gestionar jugadores y crear alineaciones tácticas de fútbol.

## Tecnologías Utilizadas

- **Backend**: Java Spring Boot 4.0.0
- **Frontend**: Angular 20
- **Base de Datos**: MySQL

## Requisitos Previos

- Java 21 o superior
- Node.js y npm
- MySQL 8.0 o superior
- Maven 3.6+

## Configuración de la Base de Datos

1. Crear la base de datos ejecutando el script SQL:
```bash
mysql -u root -p < simuladorTacticas.sql
```

2. Configurar las credenciales de MySQL en `simuladortactico/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

## Instalación y Ejecución

### Backend (Spring Boot)

1. Navegar al directorio del backend:
```bash
cd simuladortactico
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### Frontend (Angular)

1. Navegar al directorio del frontend:
```bash
cd simulador-frontend
```

2. Instalar dependencias:
```bash
npm install
```

3. Ejecutar la aplicación:
```bash
npm start
```

El frontend estará disponible en `http://localhost:4200`

## Funcionalidades

### Autenticación
- Registro de nuevos entrenadores
- Inicio de sesión

### Gestión de Jugadores
- Añadir nuevos jugadores
- Editar jugadores existentes
- Eliminar jugadores
- Listar todos los jugadores del entrenador

### Gestión de Alineaciones
- Crear nuevas alineaciones (Fútbol 7 o Fútbol 11)
- Validación automática de jugadores suficientes
- Colocar jugadores en el campo mediante drag-and-drop
- Mover jugadores libremente por el campo
- Agregar equipo contrario con formaciones predefinidas:
  - **Fútbol 7**: 1-2-3-1 y 1-3-1-2
  - **Fútbol 11**: 1-4-4-2 y 1-3-5-2
- Guardar alineaciones para uso futuro
- Editar alineaciones existentes
- Eliminar alineaciones

## Estructura del Proyecto

```
simuladorTacticas_proy/
├── simuladortactico/          # Backend Spring Boot
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/simuladortactico/
│   │       │       ├── model/         # Entidades JPA
│   │       │       ├── repository/    # Repositorios
│   │       │       ├── service/       # Lógica de negocio
│   │       │       └── controller/    # Controladores REST
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
├── simulador-frontend/        # Frontend Angular
│   ├── src/
│   │   └── app/
│   │       ├── components/   # Componentes Angular
│   │       ├── services/      # Servicios HTTP
│   │       └── models/       # Interfaces TypeScript
│   └── package.json
└── simuladorTacticas.sql    # Script de base de datos
```

## API Endpoints

### Autenticación
- `POST /api/auth/registro` - Registrar nuevo entrenador
- `POST /api/auth/login` - Iniciar sesión

### Jugadores
- `GET /api/jugadores/entrenador/{id}` - Obtener jugadores del entrenador
- `POST /api/jugadores/entrenador/{id}` - Crear nuevo jugador
- `PUT /api/jugadores/{id}` - Actualizar jugador
- `DELETE /api/jugadores/{id}` - Eliminar jugador
- `GET /api/jugadores/entrenador/{id}/count` - Contar jugadores

### Alineaciones
- `GET /api/alineaciones/entrenador/{id}` - Obtener alineaciones del entrenador
- `GET /api/alineaciones/{id}` - Obtener alineación específica
- `POST /api/alineaciones/entrenador/{id}` - Crear nueva alineación
- `PUT /api/alineaciones/{id}` - Actualizar alineación
- `DELETE /api/alineaciones/{id}` - Eliminar alineación
- `GET /api/alineaciones/entrenador/{id}/validar/{tipoFormacion}` - Validar jugadores suficientes
- `GET /api/alineaciones/formacion-contraria/{tipoFormacion}` - Obtener formación contraria
- `GET /api/alineaciones/formacion-contraria/{tipoFormacion}/{opcion}` - Obtener formación contraria alternativa

## Notas

- Asegúrate de que MySQL esté ejecutándose antes de iniciar el backend
- El backend debe estar ejecutándose antes de iniciar el frontend
- Las contraseñas se almacenan en texto plano (no recomendado para producción)
