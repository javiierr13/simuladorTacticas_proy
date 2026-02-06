import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alineacion, AlineacionRequest, Participacion } from '../models/alineacion.model';

@Injectable({
  providedIn: 'root'
})
export class AlineacionService {
  private apiUrl = 'http://localhost:8013/api/alineaciones';

  constructor(private http: HttpClient) {}

  obtenerAlineaciones(entrenadorId: number): Observable<Alineacion[]> {
    return this.http.get<Alineacion[]>(`${this.apiUrl}/entrenador/${entrenadorId}`);
  }

  obtenerAlineacion(id: number): Observable<Alineacion> {
    return this.http.get<Alineacion>(`${this.apiUrl}/${id}`);
  }

  obtenerParticipaciones(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/participaciones`);
  }

  crearAlineacion(entrenadorId: number, request: AlineacionRequest): Observable<Alineacion> {
    return this.http.post<Alineacion>(`${this.apiUrl}/entrenador/${entrenadorId}`, request);
  }

  actualizarAlineacion(id: number, request: AlineacionRequest): Observable<Alineacion> {
    return this.http.put<Alineacion>(`${this.apiUrl}/${id}`, request);
  }

  eliminarAlineacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  validarJugadores(entrenadorId: number, tipoFormacion: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/entrenador/${entrenadorId}/validar/${tipoFormacion}`);
  }

  obtenerFormacionContraria(tipoFormacion: string): Observable<Participacion[]> {
    return this.http.get<Participacion[]>(`${this.apiUrl}/formacion-contraria/${tipoFormacion}`);
  }

  obtenerFormacionContrariaAlternativa(tipoFormacion: string, opcion: number): Observable<Participacion[]> {
    return this.http.get<Participacion[]>(`${this.apiUrl}/formacion-contraria/${tipoFormacion}/${opcion}`);
  }
}
