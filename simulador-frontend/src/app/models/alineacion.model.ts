export interface Alineacion {
  id?: number;
  tipoFormacion: string;
  fechaCreacion?: string;
}

export interface Participacion {
  jugadorId?: number;
  nombreJugador?: string;
  dorsal?: number;
  posicion?: string;
  posX: number;
  posY: number;
  esEquipoContrario: boolean;
}

export interface AlineacionRequest {
  tipoFormacion: string;
  participaciones: Participacion[];
}
