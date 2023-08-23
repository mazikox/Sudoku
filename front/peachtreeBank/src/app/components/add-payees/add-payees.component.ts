import {Component} from '@angular/core';
import {ClientService} from "../../services/client.service";
import {FormControl, NgForm, Validators} from "@angular/forms";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {Router} from "@angular/router";
import {takeUntil} from "rxjs";


@Component({
  selector: 'app-add-payees',
  templateUrl: './add-payees.component.html',
  styleUrls: ['./add-payees.component.scss']
})
export class AddPayeesComponent {
  name = new FormControl('', [
    Validators.required,
    Validators.pattern(/^[A-Za-z\s]+$/)
  ]);
  address = new FormControl('', [
    Validators.required,
    Validators.pattern(/^[A-Za-z\s]+$/)
  ]);
  accountNumber = new FormControl('', [
    Validators.required,
    Validators.pattern('[0-9]{26}')
  ]);

  constructor(private clientService: ClientService, private dialog: MatDialog, private router: Router) {
  }

  addPayee(data: NgForm) {
    console.log(data.value)
    if (data.valid) {
      this.clientService.addPayee("/payees/add", data.value).pipe().subscribe((data) => {
          setTimeout(() => {
            this.router.navigate(['/payees']);
          }, 500);
        },
        (error) => {
          this.openDialog();
        });

    } else {
      this.openDialog();
    }
  }

  openDialog() {
    this.dialog.open(DialogContentExampleDialog);
  }
}

@Component({
  selector: 'dialog-content-example-dialog',
  templateUrl: 'dialog-content-example-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})

export class DialogContentExampleDialog {
}
