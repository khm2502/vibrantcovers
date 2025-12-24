"use server";

import apiClient from "@/lib/api-client";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

export const getPaymentStatus = async ({ orderId }: { orderId: string }) => {
  const { getUser } = getKindeServerSession();
  const user = await getUser();

  if (!user?.id || !user.email) {
    throw new Error("You need to be logged in to view this page.");
  }

  const order = await apiClient.getOrderStatus(orderId, user.id, user.email);

  if (!order) {
    throw new Error("This order does not exist.");
  }

  if (order.isPaid) {
    return order;
  } else {
    return false;
  }
};
