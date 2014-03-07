package org.jetbrains.codeGolf.auth;

import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetPackageClass;
import jet.runtime.typeinfo.JetValueParameter;

@JetPackageClass(abiVersion=6)
public final class AuthPackage
{
  @JetMethod(flags=16, returnType="Z")
  public static final boolean hasAdminAccess(@JetValueParameter(name="username", type="?Ljava/lang/String;") String username)
  {
    return AuthPackage.src.JBAccountAuth.528204361.hasAdminAccess(username);
  }
}