import {AbstractControl, ValidationErrors} from "@angular/forms";

export class DoubleValidator {
    static isDouble(control: AbstractControl): ValidationErrors | null {
        let value = Number.parseFloat(control.value);
        if (Number.isNaN(value)) {
            return {isNotDouble:'It is not valid double value'};
        }
        return null;
    }
}
