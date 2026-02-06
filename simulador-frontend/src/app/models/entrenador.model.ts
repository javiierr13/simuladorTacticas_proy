export interface Entrenador {
  id: number;
  nombre: string;
  correo: string;
}

export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface RegistroRequest {
  nombre: string;
  correo: string;
  contrasena: string;
}

export interface AuthResponse {
  id: number;
  nombre: string;
  correo: string;
}
