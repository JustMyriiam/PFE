import { Component, inject, OnInit } from '@angular/core';
import ItemCountComponent from '../../shared/pagination/item-count.component';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { IContract } from '../../entities/insurance/contract/contract.model';
import { ITEMS_PER_PAGE } from '../../config/pagination.constants';
import { combineLatest, Subscription } from 'rxjs';
import { ContractService } from '../../entities/insurance/contract/service/contract.service';
import { AccountService } from '../../core/auth/account.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SortService } from '../../shared/sort';
import { tap } from 'rxjs/operators';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'jhi-contracts',
  imports: [ItemCountComponent, NgbPagination, NgForOf, NgIf],
  templateUrl: './contracts.component.html',
})
export class ContractsComponent implements OnInit {
  contracts: IContract[] = [];
  login?: string;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  predicate = 'id';
  ascending = true;
  isLoading = false;
  subscription?: Subscription;

  protected contractService = inject(ContractService);
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

      this.contractService.query(pageRequest).subscribe({
        next: res => {
          this.contracts = res.body ?? [];
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
