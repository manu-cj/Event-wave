import {Component, OnDestroy, OnInit} from '@angular/core';
import {IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';

@Component({
  selector: 'app-events-list',
  imports: [],
  templateUrl: './eventsList.component.html',
})
export class EventsList implements OnInit, OnDestroy {
  eventPage: IEventPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  private destroy$ = new Subject<void>();

  constructor(
    private eventApi: EventService,
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    interval(10000)
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
