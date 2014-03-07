package org.jetbrains.codeGolf.auth;

import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

public final class AuthPackage$src$JBAccountAuth$528204361
{
  @JetMethod(flags=16, returnType="Z")
  public static final boolean hasAdminAccess(@JetValueParameter(name="username", type="?Ljava/lang/String;") String username)
  {
    return !(!Intrinsics.areEqual(username, "svtk") ? Intrinsics.areEqual(username, "nik") : 1) ? Intrinsics.areEqual(username, "fearfall") : true;
  }
}