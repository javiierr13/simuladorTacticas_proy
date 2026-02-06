import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Jugador } from '../models/jugador.model';

@Injectable({
  providedIn: 'root'
})
export class JugadorService {
  private apiUrl = 'http://localhost:8013/api/jugadores';

  constructor(private http: HttpClient) {}

  obtenerJugadores(entrenadorId: number): Observable<Jugador[]> {
    return this.http.get<Jugador[]>(`${this.apiUrl}/entrenador/${entrenadorId}`);
  }

  crearJugador(entrenadorId: number, jugador: Jugador): Observable<Jugador> {
    return this.http.post<Jugador>(`${this.apiUrl}/entrenador/${entrenadorId}`, jugador);
  }

  actualizarJugador(id: number, jugador: Jugador): Observable<Jugador> {
    return this.http.put<Jugador>(`${this.apiUrl}/${id}`, jugador);
  }

  eliminarJugador(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  contarJugadores(entrenadorId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/entrenador/${entrenadorId}/count`);
  }
}
