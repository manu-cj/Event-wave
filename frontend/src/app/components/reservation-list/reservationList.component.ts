import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {IReservationPage} from '../../models/reservation.model';
import {ReservationService} from '../../service/reservation.service';

@Component({
  selector: 'app-reservations-list',
  imports: [],
  templateUrl: './reservationsList.component.html',
})
export class ReservationsList implements OnInit, OnDestroy {
  reservationPage: IReservationPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  @Input() token!: string;
  private destroy$ = new Subject<void>();

  constructor(
    private ReservationApi: ReservationService,
  ) {}

  ngOnInit(): void {
    this.loadReservation();
    interval(10000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadReservation());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadReservation() : void {
    this.ReservationApi.getReservations(this.token, this.page, this.pageSize).subscribe({
      next: async (result: IReservationPage) => {
        this.reservationPage = result;
      }
    })
  }

  nextPage() : void {
    this.page++;
    this.loadReservation();
  }

  prevPage() : void {
    if (this.page > 0) {
      this.page--;
      this.loadReservation();
    }
  }
}
