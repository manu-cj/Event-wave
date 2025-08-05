import {Component, OnDestroy, OnInit} from '@angular/core';
import {IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {EventCard} from '../../components/event-card/eventCard.component';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-events',
  imports: [
    NavbarComponent,
    EventCard,
    FormsModule
  ],
  templateUrl: './events.html',
})
export class Events implements OnInit, OnDestroy {
  eventPage: IEventPage | null = null;
  title: string = '';
  page: number = 0;
  pageSize: number = 10;
  sortedColumn: string = 'date';
  sortedDirection: string = 'desc';
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

  // Load all events with pagination
  loadEvents() : void {
    this.eventApi.getEvents(this.title, this.page, this.pageSize, this.sortedColumn, this.sortedDirection).subscribe({
      next: async (result: IEventPage) => {
        this.eventPage = result;
        console.log(result);
      }
    })
  }

  // pagination manager
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
