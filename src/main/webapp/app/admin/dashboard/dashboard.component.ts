import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { ChartConfiguration, ChartType } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { forkJoin } from 'rxjs';
import { ContractComponent } from '../../entities/insurance/contract/list/contract.component';
import { IContract } from '../../entities/insurance/contract/contract.model';
import { ContractService } from '../../entities/insurance/contract/service/contract.service';

@Component({
  selector: 'jhi-dashboard',
  imports: [FaIconComponent, NgChartsModule, ContractComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export default class DashboardComponent implements OnInit {
  contracts = 0;
  claims = 0;
  totalUpfrontPremium = 0;
  inProgressClaims = 0;
  resolvedClaims = 0;

  doughnutChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom',
      },
    },
  };

  doughnutChartData = {
    labels: ['Resolved', 'In Progress'],
    datasets: [
      {
        data: [0, 0],
        backgroundColor: ['#0278f6', 'darkorange'],
      },
    ],
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.http.get<number>('/api/contracts/count').subscribe(count => {
      this.contracts = count;
    });

    this.http.get<number>('/api/claims/count').subscribe(count => {
      this.claims = count;
    });

    this.http.get<number>('/api/contracts/total-upfront-premium').subscribe(count => {
      this.totalUpfrontPremium = count;
    });

    forkJoin({
      resolved: this.http.get<number>('/api/claims/count?status.equals=RESOLVED'),
      inProgress: this.http.get<number>('/api/claims/count?status.equals=IN_PROGRESS'),
    }).subscribe(({ resolved, inProgress }) => {
      this.resolvedClaims = resolved;
      this.inProgressClaims = inProgress;
      this.updateChartData();
    });
  }

  private updateChartData(): void {
    this.doughnutChartData = {
      labels: ['Resolved', 'In Progress'],
      datasets: [
        {
          data: [this.resolvedClaims, this.inProgressClaims],
          backgroundColor: ['#0278f6', 'darkorange'],
        },
      ],
    };
  }
}
