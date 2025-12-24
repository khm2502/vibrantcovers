"use server";

import apiClient from "@/lib/api-client";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

export const createCheckoutSession = async ({
  configId,
}: {
  configId: string;
}) => {
  const { getUser } = getKindeServerSession();
  const user = await getUser();

  if (!user || !user.id || !user.email) {
    throw new Error("You need to be logged in");
  }

  return await apiClient.createCheckoutSession(configId, user.id, user.email);
};
