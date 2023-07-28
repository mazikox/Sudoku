import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BackComponent } from './components/back/back.component';

const routes: Routes = [
  {
    path: 'api',
    component: BackComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
