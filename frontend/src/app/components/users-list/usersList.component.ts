import {Component, OnDestroy, OnInit} from '@angular/core';
import {IUserPage} from '../../models/user.model';
import {UserService} from '../../service/user.service';
import {interval, Subject, takeUntil} from 'rxjs';

@Component({
  selector: 'app-users-list',
  imports: [],
  templateUrl: './usersList.component.html',
})

export class UsersList implements OnInit, OnDestroy {
  userPage: IUserPage | null = null;
  page: number = 0;
  pageSize: number = 10;
  private destroy$ = new Subject<void>();

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
    this.api.getUsers(this.page, this.pageSize).subscribe({
      next: async (result: IUserPage) => {
        this.userPage = result;
      }
    });
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
}
