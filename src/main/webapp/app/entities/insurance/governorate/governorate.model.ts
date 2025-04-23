export interface IGovernorate {
  id: string;
  name?: string | null;
}

export type NewGovernorate = Omit<IGovernorate, 'id'> & { id: null };
