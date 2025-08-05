import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {IUserPage} from '../../models/user.model';
import {UserService} from '../../service/user.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {LucideAngularModule, Search, Ticket, Funnel} from 'lucide-angular';

@Component({
  selector: 'app-users-list',
  imports: [
    FormsModule,
    LucideAngularModule
  ],
  templateUrl: './usersList.component.html',
})

export class UsersList implements OnInit, OnDestroy {
  userPage: IUserPage | null = null;
  param: string = '';
  page: number = 0;
  pageSize: number = 10;
  sortedColumn: string = 'createdAt';
  sortedDirection: string = 'desc';
  selectedFilter: string = '';
  @Input() token!: string;
  private destroy$ = new Subject<void>();

  // Icons
  readonly Search = Search;
  readonly Funnel = Funnel;

  constructor(
    private api: UserService,
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUsers();
    interval(10000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadUsers());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadUsers() : void {
    this.api.getUsers(this.param, this.token, this.page, this.pageSize, this.sortedColumn, this.sortedDirection).subscribe({
      next: async (result: IUserPage) => {
        this.userPage = result;
      }
    });
  }

  applySelectedFilter() : void {
    switch (this.selectedFilter) {
      case 'dateAsc':
        this.filterDateAsc();
        break;
      case 'dateDesc':
        this.filterDateDesc();
        break;
      case 'usernameAsc':
        this.filterUsernameAsc();
        break;
      case 'usernameDesc':
        this.filterUsernameDesc();
        break;
    }
  }

  filterDateAsc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadUsers();
  }

  filterDateDesc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadUsers();
  }

  filterUsernameAsc() : void {
    this.sortedColumn = 'username';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadUsers();
  }
  filterUsernameDesc() : void {
    this.sortedColumn = 'username';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadUsers();
  }

  nextPage() : void {
    this.page++;
    this.loadUsers();
  }

  prevPage() : void {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
    }
  }

  protected readonly Ticket = Ticket;
}
