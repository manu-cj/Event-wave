import {Component, OnDestroy, OnInit} from '@angular/core';
import {IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {DatePipe} from '@angular/common';
import {EventCard} from '../../components/event-card/eventCard.component';

@Component({
  selector: 'app-events',
  imports: [
    NavbarComponent,
    DatePipe,
    EventCard
  ],
  templateUrl: './events.html',
})
export class Events implements OnInit, OnDestroy {
  eventPage: IEventPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  private destroy$ = new Subject<void>();

  constructor(
    private eventApi: EventService,
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    interval(30000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadEvents());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadEvents() : void {
    this.eventApi.getEvents(this.page, this.pageSize).subscribe({
      next: async (result: IEventPage) => {
        this.eventPage = result;
      }
    })
  }

  nextPage() : void {
    this.page++;
    this.loadEvents();
  }

  prevPage() : void {
    if (this.page > 0) {
      this.page--;
      this.loadEvents();
    }
  }
}
