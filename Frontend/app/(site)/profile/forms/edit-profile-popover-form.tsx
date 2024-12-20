"use client";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
import { DialogTitle } from "@radix-ui/react-dialog";
import { useState } from "react";
import { ChangeProfile } from "../edit-profile";
import EmailForm, { emailValues } from "./edit-email";
import PhoneForm, { phoneValues } from "./edit-phone";
import UnameForm, { unameValues } from "./edit-uname";
import PsswdForm, { psswdValues } from "./edit-psswd";
import { toast } from "sonner";
import { User } from "@/interfaces";
import { gatewayClient } from "@/lib/urls";


interface EditProfilePopoverFormProps extends ChangeProfile {
  token: string,
  onUpdateSuccess: (user: User) => void
}

export default function EditProfilePopoverForm({variant, token, onUpdateSuccess}: EditProfilePopoverFormProps) {
  const [open, setOpen] = useState(false);
  const apiUrl = gatewayClient + "auth/user";
  const reqSettings = {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    }
  }

  const onEmailSubmit = async (values: emailValues) => {
    const res = await fetch(apiUrl, {
      ...reqSettings,
      body: JSON.stringify({
        email: values.email,
        password: values.password        
      })
    });
    submitResolver(res);
  }

  const onPhoneSubmit = async (values: phoneValues) => {
    const res = await fetch(apiUrl, {
      ...reqSettings,
      body: JSON.stringify({
        phone: values.phone,
        password: values.password        
      })
    });
    submitResolver(res);
  }

  const onUnameSubmit = async (values: unameValues) => {
    const res = await fetch(apiUrl, {
      ...reqSettings,
      body: JSON.stringify({
        username: values.username,
        password: values.password
      })
    });
    submitResolver(res);
  }

  const onPsswdSubmit = async (values: psswdValues) => {
    const res = await fetch(apiUrl, {
      ...reqSettings,
      body: JSON.stringify({
        newPassword: values.newPassword,
        password: values.password
      })
    });
    submitResolver(res);
  }

  const submitResolver = async (res: Response) => {
    if (res.status === 403 || res.status === 400) {
      toast.error("Wprowadzenie zmian nie powiodło się", {
        description: "Kod błędu: " + 403 + ". Błąd: " + await res.text() 
      });
    } else if (res.status === 200) {
      onUpdateSuccess(await res.json());
      toast.success("Wprowadzenie zmian przebiegło pomyślnie");
      setOpen(false);
    } else if (res.status === 500) {
      toast.error("Wprowadzenie zmian nie powiodło się", {
        description: "Błąd serwera: " + 500
      })
    } else {
      toast.error("Nietypowy błąd serwera", {
        description: "Kod błędu: " + res.status
      })
    }
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">
          Edytuj
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            Zmień {variant == "email" ? "email" : 
                   variant == "phone" ? "telefon" : 
                   variant == "psswd" ? "hasło" : "nazwę"}
          </DialogTitle>
          <DialogDescription>
            Tutaj możesz wprowadzić zmiany w {" "} 
            {variant == "email" ? "adresie email" : 
             variant == "phone" ? "numerze telefonu" : 
             variant == "psswd" ? "swoim haśle" : "swojej nazwie użytkownika"}
          </DialogDescription>
        </DialogHeader>
        {variant == "email" ? <EmailForm onSubmit={onEmailSubmit}/> :
         variant == "phone" ? <PhoneForm onSubmit={onPhoneSubmit}/> : 
         variant == "uname" ? <UnameForm onSubmit={onUnameSubmit}/> : 
         <PsswdForm onSubmit={onPsswdSubmit}/>}
      </DialogContent>
    </Dialog>
  );
}
