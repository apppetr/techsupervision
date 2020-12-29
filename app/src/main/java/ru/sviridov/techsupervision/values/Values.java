package ru.sviridov.techsupervision.values;

public interface Values {
   String MANUALLY_ADDED = "manually_added";
   String UPLOADED = "uploaded";
   String VERSION = "version";

   public interface Compensations {
      String ID = "_id";
      String LINKER = "reason_id";
      String MANUALLY_ADDED = "manually_added";
      String NAME = "name";
      String RAW_QUERY = "SELECT  * FROM compensations c where c._id in (select r2c.compensation_id from reasons2compensations r2c WHERE %s)";
      String TABLE = "compensations";
      String UPLOADED = "uploaded";
      String URI = "compensations";
      String URI_FOR_SELECTION = "select_compensations";
      String VERSION = "version";
   }

   public interface D2E {
      String DEFECT_ID = "defect_id";
      String MANUALLY_ADDED = "manually_added";
      String MAT_ELEM_ID = "mat_elem_id";
      String TABLE = "defects2elements";
      String UPLOADED = "uploaded";
      String URI = "defects2elements";
      String VERSION = "version";
   }

   public interface D2R {
      String DEFECT_ID = "defect_id";
      String MANUALLY_ADDED = "manually_added";
      String REASON_ID = "reason_id";
      String TABLE = "defects2reasons";
      String UPLOADED = "uploaded";
      String URI = "defects2reasons";
      String VERSION = "version";
   }

   public interface Defects {
      String ID = "_id";
      String LINKER = "mat_elem_id";
      String MANUALLY_ADDED = "manually_added";
      String NAME = "name";
      String TABLE = "defects";
      String UPLOADED = "uploaded";
      String URI = "defects";
      String URI_FOR_SELECTION = "select_defects";
      String VERSION = "version";
   }

   public interface E2M {
      String ELEMENT_ID = "element_id";
      String ID = "mat_elem_id";
      String MANUALLY_ADDED = "manually_added";
      String MATERIAL_ID = "material_id";
      String TABLE = "elements2materials";
      String UPLOADED = "uploaded";
      String URI = "elements2materials";
      String VERSION = "version";
   }

   public interface Elements {
      String ID = "_id";
      String MANUALLY_ADDED = "manually_added";
      String NAME = "name";
      String TABLE = "elements";
      String UPLOADED = "uploaded";
      String URI = "elements";
      String VERSION = "version";
   }

   public interface Materials {
      String ID = "_id";
      String MANUALLY_ADDED = "manually_added";
      String NAME = "name";
      String TABLE = "materials";
      String UPLOADED = "uploaded";
      String URI = "materials";
      String URI_FOR_SELECTION = "select_materials";
      String VERSION = "version";
   }

   public interface R2C {
      String COMPENSATION_ID = "compensation_id";
      String MANUALLY_ADDED = "manually_added";
      String REASON_ID = "reason_id";
      String TABLE = "reasons2compensations";
      String UPLOADED = "uploaded";
      String URI = "reasons2compensations";
      String VERSION = "version";
   }

   public interface Reasons {
      String ID = "_id";
      String LINKER = "defect_id";
      String MANUALLY_ADDED = "manually_added";
      String NAME = "name";
      String RAW_QUERY = "SELECT  * FROM reasons r where r._id in (select d2r.reason_id from defects2reasons d2r WHERE %s)";
      String TABLE = "reasons";
      String UPLOADED = "uploaded";
      String URI = "reasons";
      String URI_FOR_SELECTION = "select_reasons";
      String VERSION = "version";
   }

   public interface Versions {
      String DATE = "date";
      String ID = "id";
      String NAME = "name";
      String TABLE = "versions";
      String URI = "versions";
   }
}
