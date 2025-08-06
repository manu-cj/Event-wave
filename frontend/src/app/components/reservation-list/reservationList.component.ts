import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {IReservationPage} from '../../models/reservation.model';
import {ReservationService} from '../../service/reservation.service';
import {FormsModule} from '@angular/forms';
import {Funnel, LucideAngularModule, Search} from 'lucide-angular';

@Component({
  selector: 'app-reservations-list',
  imports: [
    FormsModule,
    LucideAngularModule
  ],
  templateUrl: './reservationsList.component.html',
})
export class ReservationsList implements OnInit, OnDestroy {
  reservationPage: IReservationPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  param: string = '';
  sortedColumn: string = 'createdAt';
  sortedDirection: string = 'desc';
  selectedFilter: string = '';
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
    this.ReservationApi.getReservations(this.param, this.page, this.pageSize, this.sortedColumn, this.sortedDirection).subscribe({
      next: async (result: IReservationPage) => {
        this.reservationPage = result;
      }
    })
  }

  applySelectedFilter() : void {
    switch (this.selectedFilter) {
      case 'dateAsc':
        this.filterDateAsc();
        break;
      case 'dateDesc':
        this.filterDateDesc();
        break;
      case 'titleAsc':
        this.filterTitleAsc();
        break;
      case 'titleDesc':
        this.filterTitleDesc();
        break;
    }
  }

  filterDateAsc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadReservation();
  }

  filterDateDesc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadReservation();
  }

  filterTitleAsc() : void {
    this.sortedColumn = 'event.title';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadReservation();
  }
  filterTitleDesc() : void {
    this.sortedColumn = 'event.title';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadReservation();
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

  protected readonly Search = Search;
  protected readonly Funnel = Funnel;
}
