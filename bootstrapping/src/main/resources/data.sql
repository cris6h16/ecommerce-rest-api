-- es accecible desde cualquier schema en este caso prod & test ( y no tenemos qe especificar como: public.unaccent(..))
CREATE EXTENSION IF NOT EXISTS unaccent SCHEMA public;
