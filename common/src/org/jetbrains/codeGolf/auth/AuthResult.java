package org.jetbrains.codeGolf.auth;

import jet.JetObject;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetClassObject;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Ljava/lang/Object;", flags=16, abiVersion=6)
public final class AuthResult
  implements JetObject
{
  private final boolean isOk;
  private final String errorMessage;
  public static final object object$ = new object();

  @JetMethod(flags=17, propertyType="Z")
  public final boolean getIsOk()
  {
    return this.isOk;
  }

  @JetMethod(flags=17, propertyType="?Ljava/lang/String;")
  public final String getErrorMessage()
  {
    return this.errorMessage;
  }

  @JetConstructor
  public AuthResult(@JetValueParameter(name="errorMessage", type="?Ljava/lang/String;") String errorMessage)
  {
    this.errorMessage = errorMessage; if (this.errorMessage != null) 1; this.isOk = false; } 
  @JetClass(signature="Ljava/lang/Object;", flags=16, abiVersion=6)
  @JetClassObject
  public static final class object implements JetObject { private final AuthResult SUCCESS = new AuthResult((String)null);

    @JetMethod(flags=17, propertyType="Lorg/jetbrains/codeGolf/auth/AuthResult;")
    public final AuthResult getSUCCESS()
    {
      return this.SUCCESS;
    }
  }
}