export interface Jugador {
  id?: number;
  nombre: string;
  dorsal: number;
  posicion: string;
  piernaDominante: 'IZQUIERDA' | 'DERECHA' | 'AMBAS';
}
