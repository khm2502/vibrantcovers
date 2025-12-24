"use server";

import apiClient from "@/lib/api-client";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

export const getAuthStatus = async () => {
  const { getUser } = getKindeServerSession();
  const user = await getUser();

  if (!user?.id || !user.email) {
    throw new Error("Invalid user data");
  }

  await apiClient.createUser(user.id, user.email);

  return { success: true };
};
