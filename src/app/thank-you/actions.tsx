"use server";

import apiClient from "@/lib/api-client";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

export const getPaymentStatus = async ({ orderId }: { orderId: string }) => {
  try {
    const { getUser } = getKindeServerSession();
    const user = await getUser();

    if (!user?.id || !user.email) {
      console.error("getPaymentStatus: user not logged in");
      return false;
    }

    if (!orderId) {
      console.error("getPaymentStatus: missing orderId");
      return false;
    }

    const order = await apiClient.getOrderStatus(orderId, user.id, user.email);

    if (!order) {
      console.error("getPaymentStatus: order not found", { orderId });
      return false;
    }

    if (order.isPaid) {
      return order;
    }

    // Not paid yet
    return false;
  } catch (error: any) {
    console.error("getPaymentStatus: error while fetching payment status", {
      orderId,
      message: error?.message,
      status: error?.status,
    });
    // Swallow the error and let the UI handle `false` / undefined gracefully
    return false;
  }
};

