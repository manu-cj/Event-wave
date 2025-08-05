import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ReservationService} from '../../service/reservation.service';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {IReservationPage} from '../../models/reservation.model';
import {interval, Subject, takeUntil} from 'rxjs';
import {environment} from '../../../environment/environment';
import {DatePipe} from '@angular/common';
import {NavbarComponent} from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-reservations',
  imports: [
    DatePipe,
    NavbarComponent
  ],
  templateUrl: './reservations.html',
  standalone: true,
})

export class Reservations implements OnInit, OnDestroy {
  token: string = '';
  reservationPage: IReservationPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  isLoading: boolean = false;
  private destroy$ = new Subject<void>();
  private baseUrl = environment.apiUrl;


  constructor(
    private reservationApi : ReservationService,
    private auth: AuthService,
  ) {
  }


  ngOnInit(): void {
    this.token = this.auth.getToken() as string;


    this.loadReservation();
    interval(30000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadReservation());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadReservation() : void {
    this.reservationApi.getUserReservations(this.token, this.page, this.pageSize).subscribe({
      next: async (result: IReservationPage) => {
        this.reservationPage = result;
        if (this.reservationPage.content.length > 0) {
          this.isLoading = true;
        }
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

  showTicket(reservationId: string) {
    this.reservationApi.downloadTicket(reservationId).subscribe(blob => {
      const url = `${this.baseUrl}/reservation/${reservationId}/ticket`;
      window.open(url, '_blank'); // Ouvre le PDF dans un nouvel onglet
      // Ou pour afficher dans une iframe :
      // this.pdfUrl = url;
    });
  }
}
