import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { JugadorService } from '../../services/jugador.service';
import { AlineacionService } from '../../services/alineacion.service';
import { Jugador } from '../../models/jugador.model';
import { Alineacion, Participacion } from '../../models/alineacion.model';

@Component({
  selector: 'app-alineaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './alineaciones.component.html',
  styleUrl: './alineaciones.component.css'
})
export class AlineacionesComponent implements OnInit {
  alineaciones: Alineacion[] = [];
  jugadores: Jugador[] = [];
  entrenadorId: number = 0;
  mostrarFormulario: boolean = false;
  alineacionEditando: Alineacion | null = null;
  tipoFormacion: string = 'FUTBOL_7';
  participaciones: Participacion[] = [];
  jugadoresDisponibles: Jugador[] = [];
  error: string = '';
  draggedPlayer: Participacion | null = null;

  constructor(
    private authService: AuthService,
    private jugadorService: JugadorService,
    private alineacionService: AlineacionService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }
    this.entrenadorId = user.id;
    this.cargarJugadores();
    this.cargarAlineaciones();
    this.validarJugadores();
  }

  cargarJugadores() {
    this.jugadorService.obtenerJugadores(this.entrenadorId).subscribe({
      next: (data) => {
        this.jugadores = data;
        this.jugadoresDisponibles = [...data];
      }
    });
  }

  cargarAlineaciones() {
    this.alineacionService.obtenerAlineaciones(this.entrenadorId).subscribe({
      next: (data) => {
        this.alineaciones = data;
      }
    });
  }

  validarJugadores() {
    this.alineacionService.validarJugadores(this.entrenadorId, this.tipoFormacion).subscribe({
      next: (data) => {
        if (!data.tieneSuficientes) {
          this.error = data.mensaje;
        } else {
          this.error = '';
        }
      }
    });
  }

  nuevoAlineacion() {
    if (this.error) {
      alert(this.error);
      return;
    }
    this.alineacionEditando = null;
    this.participaciones = [];
    this.jugadoresDisponibles = [...this.jugadores];
    this.mostrarFormulario = true;
  }

  editarAlineacion(alineacion: Alineacion) {
    this.alineacionEditando = alineacion;
    this.tipoFormacion = alineacion.tipoFormacion;
    this.alineacionService.obtenerParticipaciones(alineacion.id!).subscribe({
      next: (data) => {
        this.participaciones = data.map((p: any) => ({
          jugadorId: p.jugador?.id,
          nombreJugador: p.jugador?.nombre,
          dorsal: p.jugador?.dorsal,
          posicion: p.jugador?.posicion,
          posX: p.posX,
          posY: p.posY,
          esEquipoContrario: p.esEquipoContrario
        }));
        this.mostrarFormulario = true;
      }
    });
  }

  mostrarSelectorFormacion: boolean = false;

  agregarEquipoContrario() {
    this.mostrarSelectorFormacion = true;
  }

  seleccionarFormacionContraria(opcion: number) {
    this.alineacionService.obtenerFormacionContrariaAlternativa(this.tipoFormacion, opcion).subscribe({
      next: (data) => {
        // Filtrar participaciones existentes del equipo contrario antes de agregar nuevas
        this.participaciones = this.participaciones.filter(p => !p.esEquipoContrario);
        this.participaciones = [...this.participaciones, ...data];
        this.mostrarSelectorFormacion = false;
      }
    });
  }

  cancelarSeleccionFormacion() {
    this.mostrarSelectorFormacion = false;
  }

  onDragStart(event: DragEvent, jugador: Jugador) {
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      this.draggedPlayer = {
        jugadorId: jugador.id,
        nombreJugador: jugador.nombre,
        dorsal: jugador.dorsal,
        posicion: jugador.posicion,
        posX: 0,
        posY: 0,
        esEquipoContrario: false
      };
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'move';
    }
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    if (this.draggedPlayer) {
      const campo = event.currentTarget as HTMLElement;
      const rect = campo.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      
      const posX = Math.round((x / rect.width) * 100);
      const posY = Math.round((y / rect.height) * 100);

      const participacion: Participacion = {
        ...this.draggedPlayer,
        posX: Math.max(0, Math.min(100, posX)),
        posY: Math.max(0, Math.min(100, posY))
      };

      this.participaciones.push(participacion);
      this.jugadoresDisponibles = this.jugadoresDisponibles.filter(j => j.id !== this.draggedPlayer!.jugadorId);
      this.draggedPlayer = null;
    }
  }

  draggedIndex: number | null = null;

  onPlayerDragStart(event: DragEvent, participacion: Participacion, index: number) {
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      this.draggedIndex = index;
    }
  }

  onPlayerDragOver(event: DragEvent) {
    event.preventDefault();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'move';
    }
  }

  onPlayerDrop(event: DragEvent) {
    event.preventDefault();
    if (this.draggedIndex !== null) {
      const campo = event.currentTarget as HTMLElement;
      const rect = campo.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      
      const posX = Math.round((x / rect.width) * 100);
      const posY = Math.round((y / rect.height) * 100);

      this.participaciones[this.draggedIndex].posX = Math.max(0, Math.min(100, posX));
      this.participaciones[this.draggedIndex].posY = Math.max(0, Math.min(100, posY));
      this.draggedIndex = null;
    }
  }

  eliminarJugadorDelCampo(index: number) {
    const participacion = this.participaciones[index];
    if (!participacion.esEquipoContrario && participacion.jugadorId) {
      const jugador = this.jugadores.find(j => j.id === participacion.jugadorId);
      if (jugador) {
        this.jugadoresDisponibles.push(jugador);
      }
    }
    this.participaciones.splice(index, 1);
  }

  guardarAlineacion() {
    const request = {
      tipoFormacion: this.tipoFormacion,
      participaciones: this.participaciones
    };

    if (this.alineacionEditando) {
      this.alineacionService.actualizarAlineacion(this.alineacionEditando.id!, request).subscribe({
        next: () => {
          this.cargarAlineaciones();
          this.cancelarFormulario();
        }
      });
    } else {
      this.alineacionService.crearAlineacion(this.entrenadorId, request).subscribe({
        next: () => {
          this.cargarAlineaciones();
          this.cancelarFormulario();
        }
      });
    }
  }

  eliminarAlineacion(id: number) {
    if (confirm('¿Estás seguro de eliminar esta alineación?')) {
      this.alineacionService.eliminarAlineacion(id).subscribe({
        next: () => {
          this.cargarAlineaciones();
        }
      });
    }
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.alineacionEditando = null;
    this.participaciones = [];
    this.jugadoresDisponibles = [...this.jugadores];
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }

  cambiarTipoFormacion() {
    this.validarJugadores();
    this.participaciones = [];
    this.jugadoresDisponibles = [...this.jugadores];
  }
}
