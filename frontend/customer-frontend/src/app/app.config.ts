import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }),
    //register application routes
    provideRouter(routes),
    // Enables angular's HTTP client
    //lets angular to send requests to springboot api
    //Makes the http service available
    provideHttpClient()]
};
