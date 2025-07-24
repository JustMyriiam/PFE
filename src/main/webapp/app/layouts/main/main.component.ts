import { Component, OnInit, Renderer2, RendererFactory2, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent, NgIf],
})
export default class MainComponent implements OnInit {
  private readonly renderer: Renderer2;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);
  private readonly routesWithSidebarPrefix = ['/admin'];
  private readonly routesWithSidebarExact = [
    '/authority',
    '/client',
    '/city',
    '/governorate',
    '/client-address',
    '/claim',
    '/quote',
    '/contract',
    '/agency',
    '/document',
    '/insurance-pack',
    '/warranty',
    '/vehicle',
  ];

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  showAdminSidebar(): boolean {
    const currentUrl = this.router.url;

    return this.routesWithSidebarPrefix.some(route => currentUrl.startsWith(route)) || this.routesWithSidebarExact.includes(currentUrl);
  }

  showClientSidebar(): boolean {
    const currentUrl = this.router.url;

    return currentUrl.startsWith('/client');
  }

  showSidebar(): boolean {
    const currentUrl = this.router.url;

    return this.routesWithSidebarPrefix.some(route => currentUrl.startsWith(route));
  }
}
