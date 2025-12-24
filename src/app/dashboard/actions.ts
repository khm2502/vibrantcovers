"use server";

import apiClient from "@/lib/api-client";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

export const changeOrderStatus = async ({
  id,
  newStatus,
}: {
  id: string;
  newStatus: string;
}) => {
  const { getUser } = getKindeServerSession();
  const user = await getUser();

  if (!user?.id || !user.email) {
    throw new Error("You need to be logged in");
  }

  await apiClient.updateOrderStatus(id, newStatus.toUpperCase(), user.id, user.email);
};
