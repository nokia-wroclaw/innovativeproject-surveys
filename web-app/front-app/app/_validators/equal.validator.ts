import {Validator, AbstractControl} from '@angular/forms'

export class EqualValidator implements Validator {

    constructor(private validateEqual: string){}

    validate(c: AbstractControl): {[key: string]: any} {
        let v = c.value;
        let e = c.root.get(this.validateEqual);

        if(e && v !== e.value)
            return { validateEqual: false };
        return null;
    }

}