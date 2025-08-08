import { Component, OnInit, inject } from '@angular/core';
import { ClaimService } from '../../entities/insurance/claim/service/claim.service';
import { AccountService } from '../../core/auth/account.service';
import { IClaim } from '../../entities/insurance/claim/claim.model';
import { DatePipe, NgClass, NgForOf, NgIf } from '@angular/common';
import { ITEMS_PER_PAGE } from '../../config/pagination.constants';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SortService } from '../../shared/sort';
import { combineLatest, Subscription } from 'rxjs';
import { tap } from 'rxjs/operators';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import ItemCountComponent from '../../shared/pagination/item-count.component';

@Component({
  selector: 'jhi-claims',
  standalone: true,
  imports: [NgForOf, NgIf, DatePipe, NgClass, NgbPagination, ItemCountComponent],
  templateUrl: './claims.component.html',
})
export class ClaimsComponent implements OnInit {
  claims: IClaim[] = [];
  login?: string;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  predicate = 'id';
  ascending = true;
  isLoading = false;
  subscription?: Subscription;

  protected claimService = inject(ClaimService);
  protected accountService = inject(AccountService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);
  protected sortService = inject(SortService);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.fetchPaginatedContracts()),
      )
      .subscribe();
  }

  fillComponentAttributeFromRoute(params: ParamMap, data: any): void {
    const page = params.get('page');
    this.page = page !== null ? +page : 1;
    const sort = (params.get('sort') ?? data['defaultSort'] ?? 'id,asc').split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === 'asc';
  }

  sortState(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  fetchPaginatedContracts(): void {
    this.accountService.identity().subscribe(account => {
      const login = account?.login;
      if (!login) return;

      const pageRequest = {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sortState(),
        'client.login.equals': login,
      };

      this.isLoading = true;

      this.claimService.query(pageRequest).subscribe({
        next: res => {
          this.claims = res.body ?? [];
          this.totalItems = Number(res.headers.get('X-Total-Count'));
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        },
      });
    });
  }

  navigateToPage(page: number): void {
    this.page = page;
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {
        page,
        sort: this.sortState().join(','),
      },
    });
  }
}
