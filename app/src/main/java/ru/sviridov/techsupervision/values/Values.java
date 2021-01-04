package ru.sviridov.techsupervision.values;

public interface Values {
   public static final String MANUALLY_ADDED = "manually_added";
   public static final String UPLOADED = "uploaded";
   public static final String VERSION = "version";

   /* renamed from: ru.sviridov.techsupervision.values.Values$Compensations */
   public interface Compensations {

      /* renamed from: ID */
      public static final String f91ID = "_id";
      public static final String LINKER = "reason_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String NAME = "name";
      public static final String RAW_QUERY = "SELECT  * FROM compensations c where c._id in (select r2c.compensation_id from reasons2compensations r2c WHERE %s)";
      public static final String TABLE = "compensations";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "compensations";
      public static final String URI_FOR_SELECTION = "select_compensations";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$D2E */
   public interface D2E {
      public static final String DEFECT_ID = "defect_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String MAT_ELEM_ID = "mat_elem_id";
      public static final String TABLE = "defects2elements";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "defects2elements";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$D2R */
   public interface D2R {
      public static final String DEFECT_ID = "defect_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String REASON_ID = "reason_id";
      public static final String TABLE = "defects2reasons";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "defects2reasons";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$Defects */
   public interface Defects {

      /* renamed from: ID */
      public static final String f92ID = "_id";
      public static final String LINKER = "mat_elem_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String NAME = "name";
      public static final String TABLE = "defects";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "defects";
      public static final String URI_FOR_SELECTION = "select_defects";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$E2M */
   public interface E2M {
      public static final String ELEMENT_ID = "element_id";

      /* renamed from: ID */
      public static final String f93ID = "mat_elem_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String MATERIAL_ID = "material_id";
      public static final String TABLE = "elements2materials";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "elements2materials";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$Elements */
   public interface Elements {

      /* renamed from: ID */
      public static final String f94ID = "_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String NAME = "name";
      public static final String TABLE = "elements";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "elements";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$Materials */
   public interface Materials {

      /* renamed from: ID */
      public static final String f95ID = "_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String NAME = "name";
      public static final String TABLE = "materials";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "materials";
      public static final String URI_FOR_SELECTION = "select_materials";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$R2C */
   public interface R2C {
      public static final String COMPENSATION_ID = "compensation_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String REASON_ID = "reason_id";
      public static final String TABLE = "reasons2compensations";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "reasons2compensations";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$Reasons */
   public interface Reasons {

      /* renamed from: ID */
      public static final String f96ID = "_id";
      public static final String LINKER = "defect_id";
      public static final String MANUALLY_ADDED = "manually_added";
      public static final String NAME = "name";
      public static final String RAW_QUERY = "SELECT  * FROM reasons r where r._id in (select d2r.reason_id from defects2reasons d2r WHERE %s)";
      public static final String TABLE = "reasons";
      public static final String UPLOADED = "uploaded";
      public static final String URI = "reasons";
      public static final String URI_FOR_SELECTION = "select_reasons";
      public static final String VERSION = "version";
   }

   /* renamed from: ru.sviridov.techsupervision.values.Values$Versions */
   public interface Versions {
      public static final String DATE = "date";

      /* renamed from: ID */
      public static final String f97ID = "id";
      public static final String NAME = "name";
      public static final String TABLE = "versions";
      public static final String URI = "versions";
   }
}
