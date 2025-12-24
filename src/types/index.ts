/**
 * Type definitions for the application
 * These replace Prisma-generated types since we're using Spring Boot backend
 */

export type CaseColor = "BLACK" | "STONE" | "ROSE" | "PINK" | "GREEN";

export type OrderStatus = "AWAITING_SHIPMENT" | "SHIPPED" | "FULFILLED";

export type CaseMaterial = "SILICONE" | "POLYCARBONATE";

export type CaseFinish = "SMOOTH" | "TEXTURED";

export type PhoneModel = "IPHONE11" | "IPHONE12" | "IPHONE13" | "IPHONE14" | "IPHONE15";

export interface Configuration {
  id: string;
  width: number;
  height: number;
  imageUrl: string;
  color: CaseColor | null;
  model: PhoneModel | null;
  material: CaseMaterial | null;
  finish: CaseFinish | null;
  croppedImageUrl: string | null;
}

export interface ShippingAddress {
  id?: string;
  name: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
  state?: string | null;
  phoneNumber?: string | null;
}

export interface BillingAddress {
  id?: string;
  name: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
  state?: string | null;
}

export interface Order {
  id: string;
  configurationId: string;
  userId: string;
  amount: number;
  isPaid: boolean;
  status: OrderStatus;
  shippingAddressId?: string | null;
  billingAddressId?: string | null;
  createdAt: string;
  updated: string;
  configuration?: Configuration;
  shippingAddress?: ShippingAddress;
  billingAddress?: BillingAddress;
}



