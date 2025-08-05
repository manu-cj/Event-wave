import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {Funnel, LucideAngularModule, Search} from 'lucide-angular';

@Component({
  selector: 'app-events-list',
  imports: [
    FormsModule,
    LucideAngularModule
  ],
  templateUrl: './eventsList.component.html',
})
export class EventsList implements OnInit, OnDestroy {
  eventPage: IEventPage | null = null;
  @Input() param: string = '';
  page: number = 0;
  pageSize: number = 10;
  selectedFilter: string = '';
  sortedColumn: string = 'createdAt';
  sortedDirection: string = 'desc';
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
    this.eventApi.getEvents(this.param, this.page, this.pageSize, this.sortedColumn, this.sortedDirection).subscribe({
      next: async (result: IEventPage) => {
        this.eventPage = result;
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
    this.loadEvents();
  }

  filterDateDesc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadEvents();
  }

  filterTitleAsc() : void {
    this.sortedColumn = 'title';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadEvents();
  }
  filterTitleDesc() : void {
    this.sortedColumn = 'title';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadEvents();
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

  protected readonly Search = Search;
  protected readonly Funnel = Funnel;
}
